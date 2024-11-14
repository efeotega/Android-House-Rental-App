package com.example.rent_it;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rent_it.Model.TransactionData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class ChargeCardActivity extends AppCompatActivity {
    private EditText cardNumberField, expiryMonthField, expiryYearField, cvvField;
    private Button chargeButton;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_charge_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        price=getIntent().getStringExtra("price");
        cardNumberField = findViewById(R.id.cardNumber);
        expiryMonthField = findViewById(R.id.expiryMonth);
        expiryYearField = findViewById(R.id.expiryYear);
        cvvField = findViewById(R.id.cvv);
        chargeButton = findViewById(R.id.chargeButton);

        chargeButton.setOnClickListener(view -> chargeCard());
    }
    private void chargeCard() {
        String cardNumber = cardNumberField.getText().toString().trim();
        int expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
        int expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
        String cvv = cvvField.getText().toString().trim();

        // Create a Card object
        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

        if (card.isValid()) {
            performCharge(card);
        } else {
            Toast.makeText(this, "Invalid card details", Toast.LENGTH_LONG).show();
        }
    }

    private void performCharge(Card card) {
        Charge charge = new Charge();
        charge.setCard(card);
        charge.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()); // Replace with user's email
        charge.setAmount(Integer.parseInt(price)*100); // Amount in kobo (1000 kobo = 10 Naira)

        PaystackSdk.chargeCard(ChargeCardActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChargeCardActivity.this);
                builder.setTitle("Transaction Successful");
                builder.setMessage("Reference: " + transaction.getReference());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                saveTransactionToFirebase(transaction);
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // Optionally save transaction reference for verification
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                Toast.makeText(ChargeCardActivity.this, "Transaction Failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void saveTransactionToFirebase(Transaction transaction) {
        // Get Firebase Database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transactions");

        // Retrieve user email (you may get this from your authenticated user session)
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Replace with actual user email
        int amount = Integer.parseInt(price)*100; // Replace with actual transaction amount (in kobo)

        // Create a unique ID for the transaction
        String transactionId = transaction.getReference();

        // Format current date
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Create a data object for the transaction
        TransactionData transactionData = new TransactionData(transactionId, userEmail, date, amount);

        // Save to Realtime Database
        databaseReference.child(transactionId).setValue(transactionData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(ChargeCardActivity.this, "Transaction successfully", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ChargeCardActivity.this, "Failed to save transaction: " + e.getMessage(), Toast.LENGTH_LONG).show());
        finish();
    }
}
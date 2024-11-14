package com.example.rent_it.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent_it.Model.TransactionData;
import com.example.rent_it.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionData> transactionList;

    public TransactionAdapter(List<TransactionData> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionData transaction = transactionList.get(position);
        holder.transactionReference.setText("Reference: " + transaction.getReference());
        holder.transactionEmail.setText("Email: " + transaction.getEmail());
        holder.transactionDate.setText("Date: " + transaction.getDate());
        holder.transactionAmount.setText("Amount: ₦" + transaction.getAmount() / 100.0);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView transactionReference, transactionEmail, transactionDate, transactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionReference = itemView.findViewById(R.id.transactionReference);
            transactionEmail = itemView.findViewById(R.id.transactionEmail);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
        }
    }
}


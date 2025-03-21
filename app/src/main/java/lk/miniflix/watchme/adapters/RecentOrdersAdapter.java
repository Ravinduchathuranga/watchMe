package lk.miniflix.watchme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.miniflix.watchme.R;
import lk.miniflix.watchme.module.RecentOrderModel;

public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.RecentOrderViewHolder> {

    private List<RecentOrderModel> recentOrdersList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public RecentOrdersAdapter(List<RecentOrderModel> recentOrdersList, Context context) {
        this.recentOrdersList = recentOrdersList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_order, parent, false);
        return new RecentOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentOrderViewHolder holder, int position) {
        RecentOrderModel order = recentOrdersList.get(position);
        holder.textOrderId.setText("Order ID: " + order.getOrderId());
        holder.textCustomerName.setText("Customer: " + order.getCustomerName());
        holder.textTotalAmount.setText("Amount: $" + order.getTotalAmount());
        holder.textDate.setText("Date: " + order.getDate());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, order);
            }
        });

        // Set long-click listener
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(position, order);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return recentOrdersList.size();
    }

    public static class RecentOrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textCustomerName, textTotalAmount, textDate;

        public RecentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.textOrderId);
            textCustomerName = itemView.findViewById(R.id.textCustomerName);
            textTotalAmount = itemView.findViewById(R.id.textTotalAmount);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }

    // Click listener interface
    public interface OnItemClickListener {
        void onItemClick(int position, RecentOrderModel order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Long-click listener interface
    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, RecentOrderModel order);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
}
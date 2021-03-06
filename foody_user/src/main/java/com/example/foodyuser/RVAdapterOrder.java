package com.example.foodyuser;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class RVAdapterOrder  extends RecyclerView.Adapter<RVAdapterOrder.CardViewHolder>{

    private ArrayList<OrderItem> orders;
    private Order fatherClass;

    RVAdapterOrder(ArrayList<OrderItem> orders, Order father){
        this.orders = orders;
        fatherClass = father;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);

        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder cardViewHolder, int i) {

        cardViewHolder.name.setText(orders.get(i).getOrderName());
        cardViewHolder.price.setText(orders.get(i).getPriceString());
        cardViewHolder.quantity.setText(orders.get(i).getPiecesString());
        final int pos = cardViewHolder.getAdapterPosition();

        cardViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.get(pos).plus();
                notifyItemChanged(pos);
                fatherClass.updatePrice();
            }
        });

        cardViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.get(pos).minus();
                if(orders.get(pos).getPieces() > 0){
                    notifyItemChanged(pos);
                    fatherClass.updatePrice();
                }
                else{
                    if(orders.size() == 1){
                        fatherClass.removeItem(pos);
                        fatherClass.closeActivity();
                    } else{
                        fatherClass.removeItem(pos);
                        fatherClass.updatePrice();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView quantity;
        TextView price;
        ImageButton add;
        ImageButton remove;

        CardViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            add = itemView.findViewById(R.id.item_add_button);
            remove = itemView.findViewById(R.id.item_remove_button);
            quantity = itemView.findViewById(R.id.item_quantity);
        }
    }

}
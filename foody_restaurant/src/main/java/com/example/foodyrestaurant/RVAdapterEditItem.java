package com.example.foodyrestaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class RVAdapterEditItem extends RecyclerView.Adapter<RVAdapterEditItem.DishEdit>{

    private final ArrayList<Dish> dishes;

    public RVAdapterEditItem(ArrayList<Dish> dishes){
        this.dishes = dishes;
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public RVAdapterEditItem.DishEdit onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item_edit, viewGroup, false);
        new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.setSelection(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                                    new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.getText().length());
                    if(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.length() > 0)
                        new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                                new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete_fill_black, 0);
                    new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.setError(null);
                }else{
                    new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    if(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.length() == 0){
                        new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                                new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.setError(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                                        new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishName.getContext().getString(R.string.error_dish_name_missing));
                    }
                }
            }
        });

        new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.setSelection(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                                    new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.getText().length());
                    if(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.length() > 0)
                        new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                                new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete_fill_black, 0);
                }else{
                    new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    if(new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                            new DecimalDigitsInputFilter(5,2), new DishPriceListener()).dishDesc.length() == 0){
                    }
                }
            }
        });

        return new DishEdit(v, new DishNameEditTextListener(), new DishDescriptionEditTextListener(),
                new DecimalDigitsInputFilter(5,2), new DishPriceListener());
    }

    @Override
    public void onBindViewHolder(final RVAdapterEditItem.DishEdit dishViewHolder,final int i) {

        Context context = dishViewHolder.cardView.getContext();

        dishViewHolder.nameListener.updatePosition(dishViewHolder.getAdapterPosition());
        dishViewHolder.descriptionListener.updatePosition(dishViewHolder.getAdapterPosition());
        dishViewHolder.dishPriceListener.updatePosition(dishViewHolder.getAdapterPosition());

        dishViewHolder.dishName.setText(dishes.get(i).getDishName());
        dishViewHolder.dishDesc.setText(dishes.get(i).getDishDescription());
        Float value = dishes.get(i).getPrice();
        dishViewHolder.price.setText(String.format(Locale.UK,"%.2f",value));

        dishViewHolder.dishName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        dishViewHolder.dishName.setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dishViewHolder.dishDesc.setImeOptions(EditorInfo.IME_ACTION_DONE);
        dishViewHolder.dishDesc.setRawInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


        dishViewHolder.price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dishViewHolder.price.setSelection(dishViewHolder.price.getText().length());
                    if(dishViewHolder.price.length() > 0)
                        dishViewHolder.price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete_fill_black, 0);
                }else{
                    dishViewHolder.price.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    dishViewHolder.price.setText(String.format(Locale.UK, "%.2f",dishes.get(i).getPrice()));
                }
            }
        });


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class DishEdit extends RecyclerView.ViewHolder {
        final CardView cardView;
        final EditText dishName;
        final EditText dishDesc;
        final EditText price;
        final CircleImageView dishPicture;
        final DishNameEditTextListener nameListener;
        final DishDescriptionEditTextListener descriptionListener;
        final DecimalDigitsInputFilter decimalDigitsInputFilter;
        final DishPriceListener dishPriceListener;

        DishEdit(View itemView, DishNameEditTextListener nameListener,
                 DishDescriptionEditTextListener descriptionListener, DecimalDigitsInputFilter decimalDigitsInputFilter, DishPriceListener dishPriceListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.dish_card);
            dishPicture = itemView.findViewById(R.id.dish_image);
            dishName = itemView.findViewById(R.id.dish_name);
            dishDesc = itemView.findViewById(R.id.dish_description);
            price = itemView.findViewById(R.id.dish_price);
            this.nameListener = nameListener;
            this.descriptionListener = descriptionListener;
            this.decimalDigitsInputFilter = decimalDigitsInputFilter;
            this.dishPriceListener = dishPriceListener;

            dishName.addTextChangedListener(nameListener);
            nameListener.setEditText(dishName);
            dishDesc.addTextChangedListener(descriptionListener);
            descriptionListener.setEditText(dishDesc);

            dishName.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(dishName.getCompoundDrawables()[2]!=null){
                            if(event.getX() >= (dishName.getRight()- dishName.getLeft() - dishName.getCompoundDrawables()[2].getBounds().width())) {
                                dishName.setText("");
                            }
                        }
                    }
                    return false;
                }
            });

            dishDesc.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(dishDesc.getCompoundDrawables()[2]!=null){
                            if(event.getX() >= (dishDesc.getRight()- dishDesc.getLeft() - dishDesc.getCompoundDrawables()[2].getBounds().width())) {
                                dishDesc.setText("");
                            }
                        }
                    }
                    return false;
                }
            });

            price.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(price.getCompoundDrawables()[2]!=null){
                            if(event.getX() >= (price.getRight()- price.getLeft() - price.getCompoundDrawables()[2].getBounds().width())) {
                                price.setText("");
                            }
                        }
                    }
                    return false;
                }
            });

            price.setFilters(new InputFilter[] {decimalDigitsInputFilter});
            price.addTextChangedListener(dishPriceListener);
            dishPriceListener.setEditText(price);

        }
    }

    private class DishNameEditTextListener implements TextWatcher {
        private int position;
        private EditText editText;

        void setEditText(EditText text){
            editText = text;
        }

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            dishes.get(position).setDishName(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(dishes.get(position).getDishName().length() == 0 || !editText.hasFocus())
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete_fill_black, 0);
        }
    }

    private class DishDescriptionEditTextListener implements TextWatcher {
        private int position;
        private EditText editText;

        void setEditText(EditText text){
            editText = text;
        }

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            dishes.get(position).setDishDescription(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(dishes.get(position).getDishName().length() == 0 || !editText.hasFocus())
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete_fill_black, 0);
        }
    }

    private class DishPriceListener implements TextWatcher {
        private int position;
        private EditText editText;

        void setEditText(EditText text){
            editText = text;
        }

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            String price = editText.getText().toString();
            if(price.isEmpty())
                price = "0";
            Log.d("SWSW", price);
            dishes.get(position).setPrice(Float.parseFloat(price));
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(dishes.get(position).getDishName().length() == 0 || !editText.hasFocus())
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.delete_fill_black, 0);
        }
    }

    class DecimalDigitsInputFilter implements InputFilter {

        final Pattern mPattern;

        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}

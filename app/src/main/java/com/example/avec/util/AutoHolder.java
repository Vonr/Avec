package com.example.avec.util;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

public class AutoHolder extends RecyclerView.ViewHolder {
    public AutoHolder(@NonNull View itemView) {
        super(itemView);

        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                if (!name.equals("this$0")) {
                    field.set(
                            this,
                            itemView.findViewById(
                                    itemView
                                            .getResources()
                                            .getIdentifier(name, "id", itemView.getContext().getPackageName())));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

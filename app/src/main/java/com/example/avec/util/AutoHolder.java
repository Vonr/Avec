package com.example.avec.util;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

public class AutoHolder extends RecyclerView.ViewHolder {
    public AutoHolder(@NonNull View itemView) {
        super(itemView);

        // Loop over all defined fields in the class.
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                // Set field to be accessible
                field.setAccessible(true);

                // Get the field's name
                String name = field.getName();
                // Check field's name against "this$0"
                if (!name.equals("this$0")) {
                    // Find the identifier from the field name
                    // and set the field's value to the view
                    // corresponding to the identifier
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

package dev.qther.avec.util;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

// AutoHolder is a helper class to make it easier to create a RecyclerView.ViewHolder
// It will autopopulate fields with the same name as the ViewHolder's fields.
//
// Requirements: All fields in the AutoHolder must extend View.
//               No fields in the AutoHolder may be named "this$0"
//
// Example Usage:
//
// public class MyViewHolder extends AutoHolder {
//     ImageView nameOfView;
//     EditText nameOfOtherView;
//
//     public MyViewHolder(@NonNull View itemView) {
//         super(itemView);
//     }
// }
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
                    field.set(this,
                            itemView.findViewById(itemView
                                    .getResources()
                                    .getIdentifier(name, "id", itemView.getContext().getPackageName())));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

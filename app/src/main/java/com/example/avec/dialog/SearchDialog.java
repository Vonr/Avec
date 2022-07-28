package com.example.avec.dialog;

import android.content.Context;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import com.example.avec.R;

public class SearchDialog extends AvecDialog {
    public String query = "";

    public SearchDialog(Context ctx) {
        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setView(R.layout.dialog_search);
        this.dialog = db.create();
    }

    public void show() {
        dialog.show();
        EditText queryField = dialog.findViewById(R.id.search_query);
        queryField.setText("");

        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.confirm).setOnClickListener(v -> {
            this.query = queryField.getText().toString();
            succeed();
        });
    }
}

package com.ghost.pong;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;

public class Connection extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnection = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnection) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Connection Lost");
                builder.setMessage("You have lost connection with the server. Check your internet connection and try again.");
                builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)(context)).finish();
                        Intent intent1 = new Intent(context, Start.class);
                        context.startActivity(intent1);
                    }
                });
                builder.show();
            }
        }
    }

}

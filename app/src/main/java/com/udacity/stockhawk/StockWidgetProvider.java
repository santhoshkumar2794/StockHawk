package com.udacity.stockhawk;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.MainActivity;

import java.util.UUID;

/**
 * Created by santhosh-3366 on 26/04/17.
 */

public class StockWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_holder);
            Cursor cursor = context.getContentResolver().query(Contract.Quote.URI,null, null,null,null);
            cursor.moveToFirst();
            views.removeAllViews(R.id.widget_item);
            while (!cursor.isAfterLast()) {
                RemoteViews holder = new RemoteViews(context.getPackageName(),R.layout.stock_layout);
                String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
                String lastPrice = "$"+cursor.getFloat(Contract.Quote.POSITION_PRICE);
                holder.setTextViewText(R.id.title,symbol);
                holder.setTextViewText(R.id.current_price,lastPrice);
                views.addView(R.id.widget_item,holder);
                cursor.moveToNext();
            }
            views.setOnClickPendingIntent(R.id.widget_item, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

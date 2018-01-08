package tagliaferro.adriano.projetoposto.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import tagliaferro.adriano.projetoposto.R;

/**
 * Created by Adriano2 on 06/01/2018.
 */

public class PostoWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context, PostoWidgetProvider.class);

        int[] allWidgetsId = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int id : allWidgetsId) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            views.setRemoteAdapter(R.id.widget_list_veiculos, new Intent(context, PostoRemoteViewService.class));

            appWidgetManager.updateAppWidget(id, views);
        }
    }
}

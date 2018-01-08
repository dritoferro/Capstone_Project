package tagliaferro.adriano.projetoposto.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.model.Contract.VeiculoContract;

/**
 * Created by Adriano2 on 06/01/2018.
 */

public class PostoRemoteViewService extends RemoteViewsService {

    private static final String[] projection = {
            VeiculoContract.Columns.veiculo_id,
            VeiculoContract.Columns.veiculo_img,
            VeiculoContract.Columns.veiculo_nome
    };
    private Cursor data = null;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        RemoteViewsFactory factory = new RemoteViewsFactory() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(
                        VeiculoContract.Columns.CONTENT_URI, projection, null, null, null
                );
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (i == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(i)) {
                    return null;
                }
                RemoteViews remviews = new RemoteViews(getPackageName(), R.layout.list_item_veiculo);
                String srcImg = data.getString(data.getColumnIndex(VeiculoContract.Columns.veiculo_img));

                //TODO conseguir colocar a imagem no imageview
                Bitmap bitmap = BitmapFactory.decodeFile(srcImg);
                remviews.setImageViewBitmap(R.id.img_list_veiculo, bitmap);

                return remviews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.list_item_veiculo);
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int i) {
                if (data.moveToPosition(i)) {
                    return data.getInt(data.getColumnIndex(VeiculoContract.Columns.veiculo_id));
                }
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };

        return factory;
    }
}

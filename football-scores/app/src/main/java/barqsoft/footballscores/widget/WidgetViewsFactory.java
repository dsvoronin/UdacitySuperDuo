package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.scoresAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private ContentResolver contentResolver;
    private Cursor cursor;

    /**
     * todo more control with passing intent data of how to query
     */
    public WidgetViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.contentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Date fragmentdate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String date = mformat.format(fragmentdate);
        cursor = contentResolver.query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, new String[]{date}, null);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * Similar to getView of Adapter where instead of View
     * we return RemoteViews
     */
    @Override
    public RemoteViews getViewAt(int position) {
        cursor.moveToPosition(position);

        String home = cursor.getString(scoresAdapter.COL_HOME);
        String score = cursor.getString(scoresAdapter.COL_HOME_GOALS) + " - " + cursor.getString(scoresAdapter.COL_AWAY_GOALS);
        String away = cursor.getString(scoresAdapter.COL_AWAY);

        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteView.setTextViewText(R.id.home_team, home);
        remoteView.setTextViewText(R.id.score, score);
        remoteView.setTextViewText(R.id.away_team, away);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (cursor != null) {
            return cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));
        } else {
            return position;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

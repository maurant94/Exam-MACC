package it.uniroma1.dis.exam;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("MYINFO", "HEREHANDLE");

        new NotificationTask(params).execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private class NotificationTask extends AsyncTask<Void, Void, Void> {

        private JobParameters params;

        public NotificationTask(JobParameters params) {
            this.params = params;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //TODO CALCOLARE I PRODOTTI IN SCADENZA E MANDARE NOTIFICA
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_shopping_cart)
                            .setContentTitle("EXPIRING PRODUCTS")
                            .setContentText("5 PRODUCTS ARE EXPIRING");

            Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);
            int mNotificationId = 001;
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

            jobFinished( params, false );

            return null;
        }
    }

}

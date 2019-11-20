package gte.com.itextmosimayor.database;

//@Database(entities = {
////        ImportantMessagesData.class,
//        UnassignedMessagesData.class,
////        OpenMessagesData.class
//}, version = 3, exportSchema = false)
//public abstract class ITextMoSiMayorDatabase extends RoomDatabase {
//
//    private static ITextMoSiMayorDatabase instance;
//
////    public abstract OpenDao openDao();
////
////    public abstract ImportantDao importantDao();
//
//    public abstract UnassignedDao unassignedDao();
//
//    public static synchronized ITextMoSiMayorDatabase getInstance(Context context) {
//        if (instance == null)
//            instance = Room.databaseBuilder(context.getApplicationContext(),
//                    ITextMoSiMayorDatabase.class, "txtmayor_database")
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build();
//        return instance;
//    }
//
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private UnassignedDao unassignedDao;
////        private ImportantDao importantDao;
////        private OpenDao openDao;
//
//        private PopulateDbAsyncTask(ITextMoSiMayorDatabase db) {
//            unassignedDao = db.unassignedDao();
////            importantDao = db.importantDao();
////            openDao = db.openDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            unassignedDao.insert(new UnassignedMessagesData("50", "June 1", "099999999121", "Static content", "1", "Normal", "0", "Open"));
//            return null;
//        }
//    }
//}

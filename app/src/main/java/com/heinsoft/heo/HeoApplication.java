package com.heinsoft.heo;


import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.heinsoft.heo.bean.DaoMaster;
import com.heinsoft.heo.bean.DaoSession;
import com.heinsoft.heo.bean.MessageBeanDao;


import org.greenrobot.greendao.database.Database;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * @ClassName:	NFCApplication 
 * @Description:TODO(Application) 
 * @author:	xiaoyl
 * @date:	2013-7-10 下午4:01:27 
 *  
 */
public class HeoApplication extends VApplication {
	private static HeoApplication instance;
	public static boolean isExit = false;
	private static DaoSession daoSession;
	private static DaoMaster daoMaster;
	/** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
	public static final boolean ENCRYPTED = false;

	public HeoApplication() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		initCrash();
		//OkGo.getInstance().init(this);
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
	//	setupDatabase()
	}

	public static HeoApplication getInstance() {
		if (null == instance) {
			instance = new HeoApplication();
		}
		return instance;
	}

	/**
	 * 获取DaoMaster
	 *
	 * @param context
	 * @return
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			try{
				ContextWrapper wrapper = new ContextWrapper(context) {
				};
				MySQLiteOpenHelper helper = new MySQLiteOpenHelper(wrapper, ENCRYPTED ? "history-db-encrypted" : "history-db",null);

				daoMaster = new DaoMaster(helper.getWritableDatabase()); //获取未加密的数据库
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return daoMaster;
	}

	/**
	 * 获取DaoSession对象
	 *
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}


	private void initCrash() {
		CaocConfig.Builder.create()
				.backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
				.enabled(true) //default: true
				.showErrorDetails(true) //default: true
				.showRestartButton(true) //default: true
				.trackActivities(true) //default: false
				.minTimeBetweenCrashesMs(2000) //default: 3000
				.restartActivity(MainActivity.class) //default: null (your app's launch activity)
				.errorActivity(null) //default: null (default error activity)
				.eventListener(null) //default: null
				.apply();
	}

	public static class  MySQLiteOpenHelper extends DaoMaster.OpenHelper {
		public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
			super(context, name, factory);
		}
		@Override
		public void onUpgrade(Database db, int oldVersion, int newVersion) {
			MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

				@Override
				public void onCreateAllTables(Database db, boolean ifNotExists) {
					DaoMaster.createAllTables(db, ifNotExists);
				}

				@Override
				public void onDropAllTables(Database db, boolean ifExists) {
					DaoMaster.dropAllTables(db, ifExists);
				}
			}, MessageBeanDao.class);
		}
	}
}

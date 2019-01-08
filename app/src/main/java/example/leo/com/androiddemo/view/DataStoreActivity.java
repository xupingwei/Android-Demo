package example.leo.com.androiddemo.view;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import example.leo.com.androiddemo.R;
import example.leo.com.androiddemo.bean.Employees;
import example.leo.com.androiddemo.utils.FileUtils;
import example.leo.com.androiddemo.utils.SQLiteDbHelper;
import example.leo.com.androiddemo.utils.SpUtils;
import example.leo.com.androiddemo.utils.ToastUtil;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: DataStoreActivity
 * @Description: 数据存储
 * @Author: wanglintao
 * @CreateDate: 2019/1/3 16:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/3 16:38
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DataStoreActivity extends Activity implements View.OnClickListener {
    //文件存储
    private Button btnFileStore;
    private EditText etSpActivityDatastore;
    //sp存储
    private Button btnSpStore;
    //文件获取
    private Button btnSpGet;
    //数据库sqlite存储
    private Button btnSqliteStore;
    //数据库sqlite数据获取
    private Button btnSqliteGet;
    private TextView tvActivityDatastore;
    ///storage/emulated/0
    private String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    //文件名称
    String fileName = sdcard + "/" + "test.txt";
    //写入和读出的数据信息
    String content = "testDemo";
    private SpUtils spUtils;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datastore);
        initData();
        btnFileStore.setOnClickListener(this);
        btnSpStore.setOnClickListener(this);
        btnSpGet.setOnClickListener(this);
        btnSqliteStore.setOnClickListener(this);
        btnSqliteGet.setOnClickListener(this);
        spUtils = new SpUtils(this, "config");
    }

    private void initData() {
        btnFileStore = findViewById(R.id.btn_file_store);
        etSpActivityDatastore = findViewById(R.id.et_sp_activity_datastore);
        btnSpStore = findViewById(R.id.btn_sp_store);
        btnSpGet = findViewById(R.id.btn_sp_get);
        btnSqliteStore = findViewById(R.id.btn_sqlite_store);
        btnSqliteGet = findViewById(R.id.btn_sqlite_get);
        tvActivityDatastore = findViewById(R.id.tv_activity_datastore);
        //创建数据库表
        SQLiteDbHelper helper = new SQLiteDbHelper(getApplicationContext());
        database = helper.getWritableDatabase();
    }

    /**
     * 向数据库表中插入数据
     */
    private void insertData() {
        for (int i = 0; i < 5; i++) {
            ContentValues values = employeesToContentValues(mockEmployees(i));
            database.insert(SQLiteDbHelper.TABLE_EMPLOYEES, null, values);
        }
    }

    private String queryData() {
        StringBuilder stringBuilder = new StringBuilder();
        /**
         * 相当于 select * from table语句;
         * table:表名
         * columns：要返回的列的名字的数组。如果设置为null，返回所有列
         * selection：一个决定返回哪一行的过滤器，相当于SQL语句中的 WHERE 关键字，null返回所有行；例：emp_id > ? and id >= 1
         * selectionArgs：用于替换上一个参数中的 ？ ,顺序对应selection中？的顺序。格式限制为String格式
         * groupBy：用于设定返回行的分组方式，相当于SQL语句中的GROUP BY 关键字
         * having：决定哪一行被放到Cursor中的过滤器。如果使用了行分组，相当于SQL语句中的HAVING关键字。传递null会导致所有的行都包含在内，前提是groupBy属性也设置为null
         * orderBy：行的排列方式，相当于SQL语句中的“ORDER BY”关键字
         * limit：设置query语句返回行的数量，相当于SQL语句中的“LIMIT”关键字，传递null表示没有设置limit语句，格式为String
         */
        Cursor cursor = database.query(SQLiteDbHelper.TABLE_EMPLOYEES, null,
                null, null,
                null, null, null, null);
        // 不断移动光标获取值
        while (cursor.moveToNext()) {
            // 直接通过索引获取字段值
            int empId = cursor.getInt(0);
            // 先获取 name 的索引值，然后再通过索引获取字段值
            String empName = cursor.getString(cursor.getColumnIndex("name"));
            stringBuilder.append("empId:"+empId+",");
            stringBuilder.append("empName:"+empName+";");
        }
        // 关闭光标
        cursor.close();
        return  stringBuilder.toString();
    }

    // 构建 Employees 对象
    private Employees mockEmployees(int i) {
        Employees employees = new Employees();
        employees.id = i;
        employees.name = "emp-" + i;
        employees.tel_no = String.valueOf(new Random().nextInt(200000));
        employees.emp_id = new Random().nextInt(5);
        return employees;
    }

    // 将 Employees 对象的值存储到 ContentValues(只可存储基本类型数据，不能存储对象) 中，ContentValues 存储的键必须与数据库中的字段名一致
    private ContentValues employeesToContentValues(Employees employees) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", employees.id);
        contentValues.put("name", employees.name);
        contentValues.put("tel_no", employees.tel_no);
        contentValues.put("emp_id", employees.emp_id);
        return contentValues;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_file_store:
                // 写入文件
                FileUtils.writeFileData(fileName, content);
                //读取文件
                String result = FileUtils.readFileData(fileName);
                tvActivityDatastore.setText(result);
                break;
            case R.id.btn_sp_store:
                String etContent = etSpActivityDatastore.getText().toString();
                if (etContent.isEmpty()) {
                    ToastUtil toastUtil = new ToastUtil(this, R.layout.toast_center, "请输入存储的值");
                    toastUtil.show();
                } else {
                    spUtils.put("spContent", etContent);
                }
                break;
            case R.id.btn_sp_get:
                String spContent = spUtils.getSharedPreference("spContent", "").toString();
                tvActivityDatastore.setText(spContent);
                break;
            case R.id.btn_sqlite_store:
                //数据存储
                insertData();
                break;
            case R.id.btn_sqlite_get:
                tvActivityDatastore.setText(queryData());
                break;
        }
    }
}

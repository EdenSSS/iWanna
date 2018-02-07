package com.example.whatareyoudoing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v4.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MainActivity extends Activity implements SensorEventListener {

	/*
	 ***********传感器****************
	 */
	//加速度（x,y,z），方向(x,y,z)，陀螺仪(x,y,z)
	private TextView acc_xView, acc_yView, acc_zView, o_xView, o_yView, o_zView, g_xView, g_yView, g_zView;
	//SensorManager对象
	private  SensorManager manager;
	//加速度传感器对象aSensor，磁场传感器对象mSensor，陀螺仪传感器对象gSensor
	private Sensor aSensor, mSensor, gSensor;
	//加速度数组
	float[] accelerometerValues = new float[3];
	//磁场数组
	float[] magneticFiledValues = new float[3];
	//转换矩阵，保存方位信息
	float[] values = new float[3];
	//转换矩阵，保存加速度和磁场数据
	float[] rotate = new float[9];
	//角速度数组
	float[] gValue = new float[3];
	//GPS数据
	double[] gps = new double[6];
	
	private float a = 1;
	private float b = 1;
	private float c = 1;
	private float d = 1;
	private float e = 1;
	private float f = 1;
	private double cls;
	
	
	private Button buBegin;
	//private Button buEnd;
	//private Button buEnsure;//当要写入其他文件时，重新生成新的文件
	private EditText et_sensor;
	
	private Timer timer;
	private TimerTask tt = null;
	private  File     fi   = null;//判断文件是否存在
	private  File     fidirectory = null;//判断文件夹是否存在
	//private  FileOutputStream fos = null;//向文件中写入数据
	
	
	/*
	 ***************最新特征值选取*************** 
	 */
	private String label; 
	private long beginTime;
	static private Feature feature = new Feature();
	
	

	
	
	
	
	

	/*
	 ***********GPS*****************
	 */
	
	// 定义LocationManager对象
	//private LocationManager lManager;
	private TextView show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//acc_xView = (TextView)findViewById(R.id.textView11);
		//acc_yView = (TextView)findViewById(R.id.textView12);
		//acc_zView = (TextView)findViewById(R.id.textView13);

		//o_xView = (TextView)findViewById(R.id.textView21);
		//o_yView = (TextView)findViewById(R.id.textView22);
		//o_zView = (TextView)findViewById(R.id.textView23);


		//g_xView = (TextView)findViewById(R.id.textView31);
		//g_yView = (TextView)findViewById(R.id.textView32);
		//g_zView = (TextView)findViewById(R.id.textView33);
		
		buBegin = (Button)findViewById(R.id.button1);
		//buEnd = (Button)findViewById(R.id.button2);
		//buEnsure = (Button)findViewById(R.id.button3);
		
		//et_sensor = (EditText)findViewById(R.id.editText1);

		
		buBegin.setOnClickListener(new BeginClassListener());
        //buBegin.setVisibility(View.INVISIBLE);
        //buEnd.setOnClickListener(new EndClassListener());
        //buEnd.setVisibility(View.INVISIBLE);
        //buEnsure.setOnClickListener(new EnsureClassListener());
        
        fidirectory = new File("/sdcard/Android/SensorData");
        if(!fidirectory.exists()){
         fidirectory.mkdir();
        }
		
		//获取传感器管理服务
		manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		//获取加速度传感器
		aSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//获取磁场传感器
		mSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		//获取陀螺仪传感器
		gSensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);




		
		//获取GPS数据
		 show = (TextView)findViewById(R.id.textView4);
		 
		 /*
		 //位置监听
		 LocationListener listener = new LocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				updateView(location);
				//show.setText("纬度：" + String.valueOf(location.getLatitude()));

			}
		};
		// 获取系统LocationManager服务
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//获取provider
		String bestProvider = lManager.getBestProvider(getCriteria(), true);
		//获取最后已知位置信息
		lManager.requestLocationUpdates(bestProvider, 0, 0, listener);
		*/
		 

	}
	
	public void updateGUI(){
	     runOnUiThread(new Runnable(){
	    	 @Override
	         public void run() {
	         // TODO Auto-generated method stub
	    		//获取最大最小值
		        e=feature.oBegin[0];
		        //g_xView.setText(" a"+a);
	    		if (feature.oMax[0] < values[0]) feature.oMax[0] = values[0];
	    		if (feature.oMax[1] < values[1]) feature.oMax[1] = values[1];
	    		if (feature.oMax[2] < values[2]) feature.oMax[2] = values[2];
	    		
	    		if (feature.gMax[0] < gValue[0]) feature.gMax[0] = gValue[0];
	    		if (feature.gMax[1] < gValue[1]) feature.gMax[1] = gValue[1];
	    		if (feature.gMax[2] < gValue[2]) feature.gMax[2] = gValue[2];
	    		
	    		if (feature.oMin[0] > values[0]) feature.oMin[0] = values[0];
	    		if (feature.oMin[1] > values[1]) feature.oMin[1] = values[1];
	    		if (feature.oMin[2] > values[2]) feature.oMin[2] = values[2];
	    		
	    		if (feature.gMin[0] > gValue[0]) feature.gMin[0] = gValue[0];
	    		if (feature.gMin[1] > gValue[1]) feature.gMin[1] = gValue[1];
	    		if (feature.gMin[2] > gValue[2]) feature.gMin[2] = gValue[2];
	    		
				double sum = Math.pow(gValue[0], 2) + Math.pow(gValue[1], 2) + Math.pow(gValue[2], 2);
				if (feature.gSqrt[0] < Math.sqrt(sum)) feature.gSqrt[0] = Math.sqrt(sum);
				if (feature.gSqrt[1] > Math.sqrt(sum)) feature.gSqrt[1] = Math.sqrt(sum);
				
				sum = Math.pow(accelerometerValues[0], 2) + Math.pow(accelerometerValues[1], 2) + Math.pow(accelerometerValues[2], 2);
				if (feature.aMax < Math.sqrt(sum)-9.8) feature.aMax = Math.sqrt(sum)-9.8;


	    		// 超过间隔时间停止
			 	if (System.currentTimeMillis() - beginTime > 1500 && a==1) {
			 	
			 		String test= new String("/sdcard/Android/SensorData/test.arff");
			 		fi = new File(test);
					 boolean d2=false;
					 if(fi.exists())
						   d2 = fi.delete();
					 fi = new File(test);
						Write(fi, "@relation test");
				 		Write(fi, "@attribute o_x_begin real");
				 		Write(fi, "@attribute o_y_begin real");
				 		Write(fi, "@attribute o_z_begin real");
				 		Write(fi, "@attribute o_x_end real");
				 		Write(fi, "@attribute o_y_end real");
				 		Write(fi, "@attribute o_z_end real");
				 		Write(fi, "@attribute o_sqrt_x_y real");
				 		Write(fi, "@attribute o_delta_z real");
				 		Write(fi, "@attribute o_x_range real");
				 		Write(fi, "@attribute o_y_range real");
				 		Write(fi, "@attribute o_z_range real");
				 		Write(fi, "@attribute g_x_max real");
				 		Write(fi, "@attribute g_y_max real");
				 		Write(fi, "@attribute g_z_max real");
				 		Write(fi, "@attribute g_x_min real");
				 		Write(fi, "@attribute g_y_min real");
				 		Write(fi, "@attribute g_z_min real");
				 		Write(fi, "@attribute g_delta_max real");
				 		Write(fi, "@attribute g_delta_min real");
				 		Write(fi, "@attribute a_delta_max real");
				 		Write(fi, "@attribute 'class' {run,catch,photo,putdown}" );
						Write(fi, "@data" );
					 
			 		a=0;
			 		b=feature.oBegin[0];
			 		feature.oEnd[0] = values[0];
			 		feature.oEnd[1] = values[1];
			 		feature.oEnd[2] = values[2];
			 		
			 		double temp = Math.pow(feature.oBegin[0]-feature.oEnd[0], 2) + Math.pow(feature.oBegin[1]-feature.oEnd[1], 2);
			 		double o_sqrt_x_y = Math.sqrt(temp);
			 		float o_delta_z = feature.oEnd[2] - feature.oBegin[2];
			 		float o_x_range = feature.oMax[0] - feature.oMin[0];
			 		float o_y_range = feature.oMax[1] - feature.oMin[1];
			 		float o_z_range = feature.oMax[2] - feature.oMin[2];
                  
			 		
			 		
			 		
			 		Write(fi,
			 				feature.oBegin[0]
			 				+","+feature.oBegin[1]
					    	+","+feature.oBegin[2]
				    		+","+feature.oEnd[0]
							+","+feature.oEnd[1]
							+","+feature.oEnd[2]
			     	     	+","+o_sqrt_x_y
							+","+o_delta_z
							+","+o_x_range
							+","+o_y_range
							+","+o_z_range
							+","+feature.gMax[0]
							+","+feature.gMax[1]
							+","+feature.gMax[2]
							+","+feature.gMin[0]		
							+","+feature.gMin[1]
							+","+feature.gMin[2]
							+","+feature.gSqrt[0]
							+","+feature.gSqrt[1]
							+","+feature.aMax
							+",?"

			 				);
			 		
					System.out.println("cls"+cls);
					 
					try {
						System.out.println("222!!!");
						InputStream is = getResources().getAssets().open("action.model");
						System.out.println("333!!!");
						cls=Classifier.evaluate(is, "/sdcard/Android/SensorData/test.arff");
						System.out.println("444!!!");
						System.out.println("cls"+cls);
						if (cls == 4.0) show.setText("检测中...");
						if (cls == 0.0) show.setText("您正在跑步，将为您锁屏");
						if (cls == 1.0) show.setText("您想要接电话，将为您接通来电");
						if (cls == 2.0) show.setText("您想要拍照，将为您打开照相机");
						if (cls == 3.0) show.setText("您暂时不使用手机，将为您锁屏");


					} catch ( Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("111!!!");
					}
					
					if(tt != null) tt.cancel();
					if(timer != null) timer.cancel();
					
			 		/*try {
		                
			 			fos.write((
		                		 accelerometerValues[0]+" "+accelerometerValues[1]+" "+
		                                 +accelerometerValues[2]+" "+values[1]+" "+
		                                 values[2]+" "+values[0]+" "+gValue[0]+" "+
		                                 gValue[1]+" "+gValue[2]+' '+gps[0]+" "+
		                                 gps[1]+" "+gps[2]+" "+gps[3]+" "+
		                                 gps[4]+" "+gps[5]+'\n').getBytes());
		                 fos.flush();
		                 } catch (IOException e) {
		                         e.printStackTrace();
		                   }*/
			 			System.out.println("Wirte Files!");
		         	}
			 	}
	                 
	         });
	 }
	

	@Override
	protected void onResume() {
		//注册监听
		manager.registerListener(this, aSensor, 0);
		manager.registerListener(this, mSensor, 0);
		manager.registerListener(this, gSensor, 0);
		super.onResume();
	}

	@Override
    protected void onStop() {
        // unregister listener
		if(tt != null) tt.cancel();
		if(timer != null) timer.cancel();
		manager.unregisterListener(this);
        super.onStop();
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//加速度传感器数值
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) accelerometerValues = event.values;
		//磁场传感器数值
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) magneticFiledValues = event.values;
		//陀螺仪传感器数值
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) gValue = event.values;

		//填充旋转矩阵
		SensorManager.getRotationMatrix(rotate, null, accelerometerValues, magneticFiledValues);
		//将弧度转化为角度
		SensorManager.getOrientation(rotate, values);
		//获取各角度显示
		values[0] = (float)Math.toDegrees(values[0]);
		values[1] = (float)Math.toDegrees(values[1]);
		values[2] = (float)Math.toDegrees(values[2]);
		
		/*
		 ************************UI显示部分*****************************
		  */
	 
		double sum = Math.pow(accelerometerValues[0], 2) + Math.pow(accelerometerValues[1], 2) + Math.pow(accelerometerValues[2], 2);
		/*
		//显示重力加速度值
		acc_xView.setText("x: " + accelerometerValues[0] + '\n' 
				+ "y: " + accelerometerValues[1] + '\n' + 
				"z: " + accelerometerValues[2] + '\n' + Math.sqrt(sum));
		//acc_yView.setText("y: " + accelerometerValues[1]);
		//acc_zView.setText("z: " + accelerometerValues[2]);

		//显示方向角：x为倾斜角，y为旋转角，z为方向角
		o_xView.setText("x(倾斜角): " + values[1] + "y(旋转角): " + values[2] + "z(方向角): " + values[0]);
		//o_yView.setText("y(旋转角): " + values[2]);
		//o_zView.setText("z(方向角): " + values[0]);

		//显示角速度
		g_xView.setText("x: " + gValue[0] + "y: " + gValue[1] + "z: " + gValue[2]);
		//g_yView.setText("y: " + gValue[1]);
		//g_zView.setText("z: " + gValue[2]);*/
	}

	

	//Button控件的监听器
	class BeginClassListener implements OnClickListener{
		@Override
		public void onClick(View v) {
		// TODO Auto-generated method stub
			feature.oBegin[0] = values[0];
			feature.oBegin[1] = values[1];
			feature.oBegin[2] = values[2];


			a=1;
			c=feature.oBegin[0];
			//cls = 4.0;
			
			feature.oMax[0] = values[0];
			feature.oMax[1] = values[1];
			feature.oMax[2] = values[2];
			
			feature.oMin[0] = values[0];
			feature.oMin[1] = values[1];
			feature.oMin[2] = values[2];
			
			feature.gMax[0] = gValue[0];
			feature.gMax[1] = gValue[1];
			feature.gMax[2] = gValue[2];
			
			feature.gMin[0] = gValue[0];
			feature.gMin[1] = gValue[1];
			feature.gMin[2] = gValue[2];

			double sum = Math.pow(gValue[0], 2) + Math.pow(gValue[1], 2) + Math.pow(gValue[2], 2);
			feature.gSqrt[0] = Math.sqrt(sum);
			feature.gSqrt[1] = Math.sqrt(sum);
			sum = Math.pow(accelerometerValues[0], 2) + Math.pow(accelerometerValues[1], 2) + Math.pow(accelerometerValues[2], 2);
			feature.aMax = Math.sqrt(sum)-9.8;
			beginTime = System.currentTimeMillis();
			
			show.setText("检测中...");
			
			timer = new Timer("Light");
			tt = new TimerTask(){
		
				@Override
				public void run() {
				// TODO Auto-generated method stub
				updateGUI();
				}
			};
		
			timer.scheduleAtFixedRate(tt, 0, 10);
			
			/*
			try {
			fos = new FileOutputStream(fi.getAbsolutePath(),true);
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			//buBegin.setVisibility(View.INVISIBLE);
			//buEnd.setVisibility(View.VISIBLE);
		}
	}
	
	
	public static void Write(File f, String content) {
	    String str = new String(); //原有txt内容
	    String s1 = new String();//内容更新
	    try {
	       
	        if (f.exists()) {
	        
	        } else {
	          
	            f.createNewFile();// 不存在则创建
	        }
	        BufferedReader input = new BufferedReader(new FileReader(f));

	        while ((str = input.readLine()) != null) {
	            s1 += str+"\r\n";
	        }

	        input.close();
	        s1 += content;

	        BufferedWriter output = new BufferedWriter(new FileWriter(f));
	        output.write(s1);
	        output.close();
	    } catch (Exception e) {
	        e.printStackTrace();

	    }
	}
}

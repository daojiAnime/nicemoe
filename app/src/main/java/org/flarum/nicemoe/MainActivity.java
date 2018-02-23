package org.flarum.nicemoe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import im.delight.android.webview.AdvancedWebView;


public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener
{
	
	private Logger gLogger;
	public void configLog()
    {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        gLogger = Logger.getLogger(this.getClass());
    }
	

    private AdvancedWebView webView;
	private FrameLayout video_free;
	private View myView = null;
	//內核
    private WebChromeClient chromeClient = null;
    private WebChromeClient.CustomViewCallback myCallBack = null;

	private DrawerLayout mDrawerLayout;
	private SwipeRefreshLayout swipeRefreshLayout;

    private static final String  url = "https://www.nicemoe.com";
	String webURL, webTitle ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		initFindId(); 
		initWebViewSettings();
		//打开软件启动WebView
		webView.loadUrl(url);
        webView.setListener(this, this);

		NavigationView navView =(NavigationView) findViewById(R.id.nav_View);
		ActionBar actionBar=getSupportActionBar();
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
		}
		navView.setCheckedItem(R.id.action_settings);
		navView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener(){
				@Override
				public boolean onNavigationItemSelected(MenuItem p1)
				{
					switch (p1.getItemId())
					{			
						case R.id.share_btn:
							Intent sharingIntent = new Intent(Intent.ACTION_SEND);
							sharingIntent.setType("text/plain");
							String shareBody = webTitle + ":\n" + webURL;
							sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Flarum Community");
							sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
							startActivity(Intent.createChooser(sharingIntent, "Share using"));
							break;
						case R.id.action_settings:
							webView.clearView();
							webView.loadUrl("https://bbs.nicemoe.com");
							mDrawerLayout.closeDrawers();
							break;
						case R.id.action_reback:
							webView.clearView();
							webView.loadUrl(url);
							mDrawerLayout.closeDrawers();
							break;
						case R.id.action_chat:
							webView.clearView();
							webView.loadUrl("https://xianliao.me/website/10688?mobile=1");
							mDrawerLayout.closeDrawers();
							break;
						 case R.id.action_guanyu:
							showGuanYu();
							break;
						default:
						    mDrawerLayout.closeDrawers();
							break;
					}
					return true;
				}			
			});

    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        //展开菜单；如果菜单出现，则将项添加到动作栏中。
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{

        int id = item.getItemId();
		switch (id)
		{
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				break;
			case R.id.share_btn:
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = webTitle + ":\n" + webURL;
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Flarum Community");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share using"));
				break;
			default:
		}

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume()
	{
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause()
	{
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
	{
        webView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed()
	{
        if (myView == null)
		{
            if (webView.canGoBack())
			{
                //後退
                webView.goBack();
            }
			else
			{
                //退出
                finish();
            }
        }
		else
		{
            //關閉全屏
            chromeClient.onHideCustomView();
        }
        super.onBackPressed();
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon)
	{
		
    }

    @Override
    public void onPageFinished(String url)
	{
		webURL = webView.getUrl();
        webTitle = webView.getTitle();

    }
	
    @Override
    public void onPageError(int errorCode, String description, String failingUrl)
	{

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent)
	{

    }

    @Override
    public void onExternalPageRequest(String url)
	{

    }
	/*********************初始化某些设置***********************************/
	/**
     * 初始化網絡設置
     */
    private void initWebViewSettings()
	{
        WebSettings webSettings = webView.getSettings();
        //可以有緩存
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //設置支持頁面js可用
        webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginState(WebSettings.PluginState.ON);
        //設置允許訪問文件數據
        webSettings.setAllowFileAccess(true);
        //可以使用localStorage
        webSettings.setDomStorageEnabled(true);
        //可以有數據庫
//        webSettings.setdatabaseEnabled(true);
        //設置定位的數據庫路徑，若不設置定位數據庫路徑則無法使用定位功能
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        //啟用地理定位
        webSettings.setGeolocationEnabled(true);

		CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg)
				{
					AdvancedWebView newWebView = new AdvancedWebView(MainActivity.this);
					WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
					newWebView.addHttpHeader("Referer","nicemoe.com");
					transport.setWebView(newWebView);
					resultMsg.sendToTarget();
					return true;
				}
				
				@Override
				public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
				{
					super.onShowCustomView(view, callback);
					if (myView != null)
					{
						callback.onCustomViewHidden();
						return;
					}
					
					myView = view;
					myCallBack = callback;
					//隱藏導航欄
//					cardView.removeView(swipeRefreshLayout);
					//隱藏網頁
//					cardView.removeView(webView);
					//隐藏网页
					webView.setVisibility(View.GONE);
					//添加视频
					video_free.addView(myView);
					//显示framelayout
					video_free.setVisibility(View.VISIBLE);
					//设置顶层
					video_free.bringToFront(); 	
					//设置横屏 	
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					//设置全屏 	
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
				}

				@Override
				public void onHideCustomView()
				{
					webView.setVisibility(View.VISIBLE);
					if (myView == null)
					{
						return;
					}
					//隱藏視頻
					video_free.removeView(myView);
					video_free.setVisibility(View.GONE);
					myCallBack.onCustomViewHidden(); 
					
					myView = null;
					// 设置竖屏 	
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
					// 取消全屏 	
//					WindowManager.LayoutParams attrs = getParent().getWindow().getAttributes(); 	
//					attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
//					getWindow().setAttributes(attrs); 
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); 	
					super.onHideCustomView();
				}


			});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
		else
		{
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon)
				{
					swipeRefreshLayout.setRefreshing(true);
				}
				public void onPageFinished(WebView view, String url)
				{
					swipeRefreshLayout.setRefreshing(false);
				}
			});

        webView.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
					{
						webView.goBack();
						return true;
					}
					return false;
				}
			});

        webView.setThirdPartyCookiesEnabled(true);
        webView.setCookiesEnabled(true);

    }
	private void initFindId()
	{
		video_free = (FrameLayout) findViewById(R.id.video_free);
        webView = (AdvancedWebView) findViewById(R.id.newWeb);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_layout);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);


		//设置刷新按钮
		swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					refreshWebView();
				}
			});
	}
	//开启一个线程执行刷新任务
	private void refreshWebView()
	{
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					
					runOnUiThread(new Runnable(){
							@Override
							public void run()
							{
								webView.reload();
								swipeRefreshLayout.setRefreshing(false);
							}
						});
				}
			}).start();
	}
	//显示关于里面
	private void showGuanYu()
	{
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("更新详情");
		dialog.setMessage(
		"\n|2.1.2："+"  修复网页自定义播放器导致软件奔溃，WebViewChromeClient重写，不断测试。又将出现的退出全屏程序崩坏bug反复测试，目前已解决"+
		"\n\n\nPS:如若存在问题请联系大D反馈处理！");
		dialog.setPositiveButton("了解", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
				}
			});
		dialog.show();
	}
}



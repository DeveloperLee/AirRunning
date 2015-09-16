package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.rondo.airrunning.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

public class SoundPlayer {
	
	private SoundPool pool;
	private Handler mHandler;
	
	private float volumn;
	private static final int MAXIMUM = 1;
	private static final long KILL_INTERVAL = 2000L;
	private static final long SOUND_INTERVAL = 700L;
	private static final float RATE = 1F;
	public static final String RUN_START = "run_start";
	public static final String RUN_PAUSE = "run_pause";
	public static final String RUN_CONTINUE = "run_continue";
	private static final String HOUR = "hour";
	private static final String MINUTE = "minute";
	private static final String SECOND = "second";
	private static final String TOTAL_TIME	= "total_time";
	
	private int[] numbers = new int[]{R.raw.zero,R.raw.one,R.raw.two,R.raw.three,R.raw.four,
			R.raw.five,R.raw.six,R.raw.seven,R.raw.eight,R.raw.nine,R.raw.ten};
	private String[] times_txt = new String[]{HOUR,MINUTE,SECOND};
	
	private Map<String,Integer> voice_map;
	private Map<Integer,Integer> number_voice_map;
	private Vector<Integer> killQueue;
	
	public SoundPlayer(Context context){
		pool = new SoundPool(MAXIMUM, AudioManager.STREAM_MUSIC, 0);
		voice_map = new HashMap<String,Integer>();
		number_voice_map = new HashMap<Integer,Integer>();
		killQueue = new Vector<Integer>();
		mHandler = new Handler();
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int maximum_volumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int current_volumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		volumn = maximum_volumn / current_volumn;
		preLoadVoices(context);
	}
	
	/**
	 * Load the voices into SoundPool
	 */
	private void preLoadVoices(Context context){
		voice_map.put(RUN_START,pool.load(context, R.raw.start, 1));
		voice_map.put(RUN_PAUSE, pool.load(context, R.raw.pause, 1));
		voice_map.put(RUN_CONTINUE, pool.load(context, R.raw.continueone, 1));
		voice_map.put(HOUR,pool.load(context, R.raw.hour, 1));
		voice_map.put(MINUTE, pool.load(context, R.raw.minute, 1));
		voice_map.put(SECOND, pool.load(context, R.raw.second, 1));
		voice_map.put(TOTAL_TIME, pool.load(context, R.raw.time, 1));
		
		for(int i = 0;i<11;i++){
	      number_voice_map.put(i, pool.load(context,numbers[i], 1));
		}
		
	}
	
	/**
	 * Plays a single sound 
	 * @param name key of the map
	 */
	public void playSingleVoice(String name){
		
	    pool.play(voice_map.get(name), volumn, volumn, 0, 0, RATE);
	    
	}
	
	/**
	 * Plays multiple sounds in sequence
	 * @param sounds the sequence of the sounds 
	 */
	public void playMultiVoice(List<Integer> sounds){
		
		for(int i = 0;i<sounds.size();i++){
			pool.play(sounds.get(i), volumn, volumn, 0, 0, RATE);
			recycle(sounds.get(i));
		}
		
	}
	
	/**
	 * Converts the total seconds in integer into the voice resource ids
	 * contains in the voice map.
	 * @param seconds  -- total seconds in integer // example : 1min30s -> 90(@param seconds)
	 * @return -- a list of the voice resource ids
	 */
	public List<Integer> readTime(int seconds){
		Map<String,Integer> time_map = TimerUtils.splitTime(seconds);
		List<Integer> sounds = new ArrayList<Integer>();
		 
		int[] times = new int[]{time_map.get(TimerUtils.HOUR),
				time_map.get(TimerUtils.MINUTE),
				time_map.get(TimerUtils.SECOND)};
		sounds.add(voice_map.get(TOTAL_TIME));
		
		for(int i = 0;i<times.length;i++){
			
		   if(times[i] == 0){
			  continue;
		  }
		   else if(times[i]>0 && times[i]<=10){
		   sounds.add(number_voice_map.get(times[i]));
		  }else if(times[i]>10 && times[i]<20){
		   sounds.add(number_voice_map.get(10));
		   sounds.add(number_voice_map.get(times[i]-10));
		  }else{
		   int mod = (int) Math.floor(times[i]/10);
		   sounds.add(number_voice_map.get(mod));
		   sounds.add(number_voice_map.get(10));
		   sounds.add(number_voice_map.get(times[i]-mod*10));
		  }
		  sounds.add(voice_map.get(times_txt[i]));
		 }
		
		return sounds;
	}
	
	/**
	 * Manage the sequence of the sound, stop the played sound
	 * @param soundID
	 */
	private void recycle(int soundID){
		killQueue.add(soundID);
		
		try {
			Thread.sleep(SOUND_INTERVAL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mHandler.postDelayed(new Runnable(){
			@Override
			public void run() {
				if(!killQueue.isEmpty()){
					pool.stop(killQueue.firstElement());
				}
			}
		}, KILL_INTERVAL);
	}

}

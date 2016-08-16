package jdumper;
import java.util.*;

import jdumper.stat.ApplicationProtocolStat;
import jdumper.stat.FreeMemStat;
import jdumper.stat.JDStatisticsTaker;
import jdumper.stat.NetworkProtocolStat;
import jdumper.stat.PacketStat;
import jdumper.stat.TransportProtocolStat;

public class JDStatisticsTakerLoader
{
	static ArrayList<JDStatisticsTaker> stakers=new ArrayList<JDStatisticsTaker>();
	
	static void loadStatisticsTaker(){
		stakers.add(new PacketStat());
		stakers.add(new NetworkProtocolStat());
		stakers.add(new TransportProtocolStat());
		stakers.add(new ApplicationProtocolStat());
		stakers.add(new FreeMemStat());
	}
	
	public static List<JDStatisticsTaker> getStatisticsTakers(){
		return stakers;
	}
	
	public static JDStatisticsTaker getStatisticsTakerAt(int index){
		return stakers.get(index);
	}
}

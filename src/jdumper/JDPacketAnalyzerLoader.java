package jdumper;
import java.util.*;

import jdumper.analyzer.*;

public class JDPacketAnalyzerLoader
{
	static List<JDPacketAnalyzer> analyzers=new ArrayList<JDPacketAnalyzer>();
	static List<List<JDPacketAnalyzer>> layerAnalyzers=new ArrayList<List<JDPacketAnalyzer>>();
	
	static void loadDefaultAnalyzer(){
		analyzers.add(new PacketAnalyzer());
		analyzers.add(new EthernetAnalyzer());
		analyzers.add(new IPv4Analyzer());
		analyzers.add(new IPv6Analyzer());
		analyzers.add(new TCPAnalyzer());
		analyzers.add(new UDPAnalyzer());
		analyzers.add(new ICMPAnalyzer());
		analyzers.add(new HTTPAnalyzer());
		analyzers.add(new FTPAnalyzer());
		analyzers.add(new TelnetAnalyzer());
		analyzers.add(new SSHAnalyzer());
		analyzers.add(new SMTPAnalyzer());
		analyzers.add(new POP3Analyzer());
		analyzers.add(new ARPAnalyzer());
		
		for(int i=0;i<10;i++)
			layerAnalyzers.add(new ArrayList<JDPacketAnalyzer>());
		
		for(JDPacketAnalyzer a:analyzers)
			layerAnalyzers.get(a.layer).add(a);
	}
	
	public static List<JDPacketAnalyzer> getAnalyzers(){
		return analyzers;
	}
	
	public static List<JDPacketAnalyzer> getAnalyzersOf(int layer){
		return layerAnalyzers.get(layer);
	}
}

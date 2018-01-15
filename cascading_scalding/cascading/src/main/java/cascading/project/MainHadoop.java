/*
 *
 */

package cascading.project;

import java.util.Properties;

import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.pipe.Pipe;
import cascading.property.AppProps;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tap.hadoop.Lfs;

/**
 * A Hello World example app that copies data from one location to another.
 * <p/>
 * It assumes the input files are TSV with headers on the first line.
 */
public class MainHadoop
  {
  public static void main( String[] args )
    {
    String inPath = args[ 0 ];
    String outPath = args[ 1 ];

    Properties properties = new Properties();
    AppProps.setApplicationJarClass( properties, MainHadoop.class );
    Hadoop2MR1FlowConnector flowConnector = new Hadoop2MR1FlowConnector( properties );

    // create the source tap
    // 输入为集群文件系统
    //Tap inTap = new Hfs( new TextDelimited( true, "\t" ), inPath );
    // 输入为本地文件系统
    Tap inTap = new Lfs( new TextDelimited( true, "\t" ), inPath );

    // create the sink tap
    // 输出为集权文件系统
    //Tap outTap = new Hfs( new TextDelimited( true, "\t" ), outPath );
    // 输出为本地文件系统
    Tap outTap = new Lfs( new TextDelimited( true, "\t" ), outPath );

    // specify a pipe to connect the taps
    Pipe copyPipe = new Pipe( "copy" );

    // connect the taps, pipes, etc., into a flow
    FlowDef flowDef = FlowDef.flowDef()
      .addSource( copyPipe, inTap )
      .addTailSink( copyPipe, outTap );

    // run the flow
    flowConnector.connect( flowDef ).complete();
    }
  }

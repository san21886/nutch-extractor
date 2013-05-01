/**
 * 
 */
package extractor;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.util.NutchConfiguration;

/**
 * @author santosh
 *
 */
public class NutchExtractor {

	public static void main(String [] args) throws IOException{
		Configuration conf=NutchConfiguration.create();
		FileSystem fs=FileSystem.get(conf);
		
		String segment=args[0];
		String outDir=args[1];
		
		Path file=new Path(segment, Content.DIR_NAME+ "/part-00000/data");
		SequenceFile.Reader reader=new SequenceFile.Reader(fs,file,conf);
		
		Text key=new Text();
		Content content=new Content();
		
		while(reader.next(key, content)){
			String filename = key.toString().replaceFirst("http://", "").replaceAll("/", "___").trim();
			File f = new File(new File(outDir).getCanonicalPath() + "/" + filename);
			FileOutputStream fos = new FileOutputStream(f);
            fos.write(content.getContent());
            fos.close();
            System.out.println(f.getAbsolutePath());
		}
		reader.close();
        fs.close();
	}
}

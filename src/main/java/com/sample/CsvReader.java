package com.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.prefs.CsvPreference;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;

public class CsvReader {
	// 全レコードを一度に読み込む場合
    public static <T> List<T> ReadAll(String csvfile, Class<T> beantype) throws IOException {

    		CsvAnnotationBeanReader<T> csvReader = new CsvAnnotationBeanReader<>(
    				beantype,
                Files.newBufferedReader(new File(csvfile).toPath(), Charset.forName("Windows-31j")),
                CsvPreference.STANDARD_PREFERENCE);

    		List<T> list = csvReader.readAll();

        csvReader.close();
        
        return list;
    }

    // レコードを1件ずつ読み込む場合
    public static <T> List<T> Read(String csvfile, Class<T> beantype) throws IOException {

        CsvAnnotationBeanReader<T> csvReader = new CsvAnnotationBeanReader<>(
        			beantype,
                Files.newBufferedReader(new File(csvfile).toPath(), Charset.forName("Windows-31j")),
                CsvPreference.STANDARD_PREFERENCE);

        List<T> list = new ArrayList<>();

        // ヘッダー行の読み込み
        String headers[] = csvReader.getHeader(true);

        T record = null;
        while((record = csvReader.read()) != null) {
            list.add(record);
        }

        csvReader.close();
        
        return list;
    }

    // レコードを1件ずつ読み込む場合
    public final static Map<String, List<String>> CsvToMap(String csvfile) throws IOException  {

    	Map<String, List<String>> map = new HashMap<String, List<String>>();
        try(BufferedReader br = new BufferedReader(new FileReader(csvfile))) {
            String line = "";
            String[] headers = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
            		if (i == 0) {
            			headers = line.split(",");
            			for (int n = 0; n < headers.length; n++) {
            				map.put(headers[n], new ArrayList<String>());
            			}
            			i++;
            			continue;
            		}
            		String[] body = line.split(",");
            		for (int n = 0; n < body.length; n++) {
        				List<String> list = map.get(headers[n]);
        				list.add(body[n]);
        				map.replace(headers[n], list);
        			}
            }
        } catch (FileNotFoundException e) {
          //Some error logging
        }
        return map;
    }
    
    public static void main(String[] args) {
    		try {
				//List<UserCsv> list = ReadAll("sample.csv", UserCsv.class);
				//List<UserCsv> list = Read("sample.csv", UserCsv.class);
    				Map<String, List<String>> map = CsvToMap("sample.csv");
    				System.out.println(map.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
}

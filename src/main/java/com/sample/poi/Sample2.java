package com.sample.poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Sample2 {

    public static void main(String[] args) throws IOException {

        // 変更元を取込
        FileInputStream in 
            = new FileInputStream("/Users/kjin/DecisionManager/Sample1.xlsx");

        Workbook book = null;
        try {
	    // 今回、WorkBookはWorkbookFactoryを使って作成します
	    book = WorkbookFactory.create(in);

	} catch (EncryptedDocumentException e1) {
	    e1.printStackTrace();

	} catch (InvalidFormatException e1) {
   	    e1.printStackTrace();
	}

	// 「サンプル」という名前のシートを取得
	Sheet sheet = book.getSheet("サンプル");

	// 1行目取得 ※Excel上、行番号は1からスタートしてますが、
	// ソース内では0からのスタートになっているので要注意！
	Row row = sheet.getRow(0);

	// 1つ目のセルを取得 ※行と同じく、0からスタート
	Cell a1 = row.getCell(0);    // Excel上、「A1」の場所

	// 値をセット
	a1.setCellValue("POIのテスト_変更後");

	// ここから出力処理
	FileOutputStream out = null;
	try {
	    // 出力先のファイルを指定
	    out = new FileOutputStream("/Users/kjin/DecisionManager/Sample1.xlsx");
	    // 上記で作成したブックを出力先に書き込み
	    book.write(out);

	} catch (FileNotFoundException e) {
	    System.out.println(e.getStackTrace());

	} finally {
	    // 最後はちゃんと閉じておきます
	    out.close();
	    book.close();
	}
    }
}
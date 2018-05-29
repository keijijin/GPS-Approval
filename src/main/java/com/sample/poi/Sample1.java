package com.sample.poi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Sample1 {

  public static void main(String[] args) throws IOException {

    // Excel2007以降の「.xlsx」形式のファイルの素を作成
    Workbook book = new XSSFWorkbook();

    // シートを「サンプル」という名前で作成
    Sheet sheet = book.createSheet("サンプル");

    // 1行目作成 ※Excel上、行番号は1からスタートしてますが、
    // ソース内では0からのスタートになっているので要注意！
    Row row = sheet.createRow(0);

    // 1つ目のセルを作成 ※行と同じく、0からスタート
    Cell a1 = row.createCell(0);  // Excel上、「A1」の場所

    // 値をセット
    a1.setCellValue("POIのテスト");

    // セルのスタイルを変えてみようと思います・・・
    CellStyle style =  book.createCellStyle();
    // フォント
    Font font = book.createFont();
    // ROSE色にしてみる
    // IndexedColorsにはいろんな色が設定されてます
    font.setColor(IndexedColors.ROSE.getIndex());
    // セルにセット！！
    style.setFont(font);
    a1.setCellStyle(style);

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
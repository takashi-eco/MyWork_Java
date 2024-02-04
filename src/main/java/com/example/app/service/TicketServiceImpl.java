package com.example.app.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Order;
import com.example.app.domain.PaymentType;
import com.example.app.domain.Ticket;
import com.example.app.domain.Weather;
import com.example.app.mapper.OrderMapper;
import com.example.app.mapper.TicketMapper;
import com.example.app.mapper.WeatherMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

	private final TicketMapper ticketMapper;
	private final OrderMapper orderMapper;
	private final WeatherMapper weatherMapper;

	@Override
	public List<Ticket> getGamesList() throws Exception {
		return ticketMapper.selectAllGames();
	}

	@Override
	public List<Ticket> getTicketsByGameId(Integer id) throws Exception {
		return ticketMapper.selectByGameId(id);
	}

	@Override
	public Ticket getTicketByTicketId(Integer id) throws Exception {
		return ticketMapper.selectByTicketId(id);
	}

	// 支払方法のリスト
	@Override
	public List<PaymentType> getPaymentTypeList() throws Exception {
		List<PaymentType> paymentTypeList = new ArrayList<>();
		paymentTypeList.add(new PaymentType(1, "クレジットカード払い"));
		paymentTypeList.add(new PaymentType(2, "コンビニ払い"));
		return paymentTypeList;
	}

	// 注文情報を登録
	@Override
	public void addOrder(Order order) throws Exception {
		orderMapper.insert(order);
	}

	// 在庫数チェック
	@Override
	public Ticket checkStockNum(Integer id) throws Exception {
		return ticketMapper.selectStockNumByticketId(id);
	}

	// 在庫数を購入数分減算して登録
	@Override
	public void subOrderNumFromStockNum(Ticket ticket) throws Exception {
		ticketMapper.updateStockNum(ticket);

	}

	// ページネーション用
	@Override
	public List<Ticket> getGamesListByPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return ticketMapper.selectLimitedGames(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		double totalNum = (double) ticketMapper.count();
		return (int) Math.ceil(totalNum / numPerPage);
	}

	@Override
	public void askWeather() throws Exception {

		Weather weather1 = weatherMapper.selectWeatherUpdateAt();
		LocalDateTime update = (LocalDateTime) weather1.getUpdatedAt();
		LocalDateTime nextUpdate = update.plusDays(1);
		LocalDateTime now = LocalDateTime.now().withNano(0);

		if (now.isAfter(nextUpdate)) {

			final String PROPERTY_FILE = "mysettings.env";
			List<Weather> weatherList = new ArrayList<Weather>();

			String apiKey = "";
			Properties property = new Properties();

				property.load(new FileInputStream(PROPERTY_FILE));

				apiKey = property.getProperty("API_KEY");

			String strUrl = "https://api.openweathermap.org/data/2.5/forecast?q=tokyo&units=metric&lang=ja&appid="
					+ apiKey;
			
			HttpURLConnection connection = null;
			InputStream in = null;
			InputStreamReader inReader = null;
			BufferedReader br = null;

				URL url = URI.create(strUrl).toURL();

				// Connectionを取得
				connection = (HttpURLConnection) url.openConnection();
				// リクエストのメソッドを指定
				connection.setRequestMethod("GET");
				// 通信開始
				connection.connect();

				// レスポンスコードを取得（200:成功）
				int iStatus = connection.getResponseCode();
				if (iStatus == HttpURLConnection.HTTP_OK) {
					// データを取得（バイト型）
					in = connection.getInputStream();

					// テキスト形式の取得
					inReader = new InputStreamReader(in);
					br = new BufferedReader(inReader);

					// テキストを取得
					String strLine;
					StringBuilder sbSentence = new StringBuilder();
					while ((strLine = br.readLine()) != null) {
						sbSentence.append(strLine);
					}

					Integer id = 1;
					String main = "";
					LocalDateTime date;
					LocalDateTime updatedAt;
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					// JSONオブジェクトのインスタンス作成
					JSONObject jsonObj = new JSONObject(sbSentence.toString());
					// キー"users"の値（JSON配列オブジェクト）をパース
					JSONArray items = jsonObj.getJSONArray("list");
					for (int i = 0; i < items.length(); i++) {
						// JSONオブジェクトをパース
						JSONObject item = items.getJSONObject(i);
						JSONArray weathers = item.getJSONArray("weather");
						for (int j = 0; j < weathers.length(); j++) {
							JSONObject weather = weathers.getJSONObject(j);
							main = (String) weather.get("main").toString();
						}
						// 各キーの値を出力
						var utcDate = LocalDateTime.parse((CharSequence) item.get("dt_txt"), dtf);
						var utcOffsetDate = utcDate.atZone(ZoneOffset.UTC);
						var jstOffsetDate = utcOffsetDate.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
						date = jstOffsetDate.toLocalDateTime();
						updatedAt = LocalDateTime.now().withNano(0);
						weatherList.add(new Weather(id, date, main, updatedAt));
						id++;
					}
					System.out.println(weatherList);
					
					weatherMapper.updateWeather(weatherList);
				}

			
		}

	}

}

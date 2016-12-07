package ru.firsto.lentaparser.data.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import ru.firsto.lentaparser.data.model.ArticleResponse;
import rx.Observable;

public interface ArticlesService {

    String ENDPOINT = "https://lenta.ru/";

    @GET("rss")
    Observable<ArticleResponse> getResponse();

    class Creator {
        public static ArticlesService newArticlesService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ArticlesService.ENDPOINT)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(ArticlesService.class);
        }
    }
}
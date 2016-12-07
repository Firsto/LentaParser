package ru.firsto.lentaparser.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict = false)
public class ArticleResponse {

    @Element
    public Channel channel;

    @Root(name = "channel", strict = false)
    public static class Channel {

        @ElementList(inline = true, name = "item")
        public List<Article> articles;
    }
}
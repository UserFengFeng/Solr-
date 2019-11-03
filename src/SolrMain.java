import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolrMain {
    public static void main(String[] args) throws IOException, SolrServerException {
        // 添加索引
        // addDoc();
        // 根据id删除索引
        // deleteDoc();
        // 条件查询
        // queryCondition();
        /*
         *  价格区间查询
         *  排序查询
         *  分页查询
         * */
        //queryCondition2();
        // 高亮查询
        queryFl();
    }

    /*
     * 向solr索引库中添加索引库的文档
     * */
    public static void addDoc() throws IOException, SolrServerException {
        String solrUrl = "http://localhost:8983/solr/demoInstance";
        //创建solr客户端对象
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(6000).build();
        // 创建文档对象
        SolrInputDocument sd = new SolrInputDocument();
        sd.addField("id", "c0001");
        sd.addField("product_name", "solrDemo");
        sd.addField("product_catalog_name", "IT技术");
        sd.addField("product_description", "拓新教育的每一个课程都是用心在做。");
        sd.addField("product_price", 299);
        sd.addField("product_picture", "111.jpg");

        // 把文档加入服务器
        client.add(sd);
        // 提交
        client.commit();
    }

    public static void deleteDoc() throws IOException, SolrServerException {

        String solrUrl = "http://localhost:8983/solr/demoInstance";
        //创建solr客户端对象
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(6000).build();
        // 根据id删除
        // client.deleteById("c0001");
        // 删除查询到的所有域
        client.deleteByQuery("product_name:青蛙");
        client.commit();
    }

    // 条件查询
    public static void queryCondition() throws IOException, SolrServerException {
        String solrUrl = "http://localhost:8983/solr/demoInstance";
        //创建solr客户端对象
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(6000).build();
        // 创建solr查询对象
        SolrQuery sq = new SolrQuery();
        // 设置查询条件
        sq.set("q", "product_name:青蛙 AND product_keywords:吸盘 OR product_catalog_name:不同");
        // 查询
        QueryResponse query = client.query(sq);
        // 获得查询结果
        SolrDocumentList results = query.getResults();
        // 获得查询的记录数
        long numFound = results.getNumFound();
        System.out.println("记录数：" + numFound);
        for (SolrDocument sd : results) {
            // 获得文档域
            String id = (String) sd.getFieldValue("id");
            String product_catalog_name = (String) sd.getFieldValue("product_catalog_name");
            Double product_price = (Double) sd.getFieldValue("product_price");
            String product_name = (String) sd.getFieldValue("product_name");
            String product_picture = (String) sd.getFieldValue("product_picture");
            System.out.println("id" + id);
            System.out.println("描述" + product_catalog_name);
            System.out.println("单价" + product_price);
            System.out.println("商品名称" + product_name);
            System.out.println("商品图片" + product_picture);
            System.out.println("------------------------");
        }
    }

    // 价格区间查询
    public static void queryCondition2() throws IOException, SolrServerException {
        String solrUrl = "http://localhost:8983/solr/demoInstance";
        //创建solr客户端对象
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(6000).build();
        // 创建solr查询对象
        SolrQuery sq = new SolrQuery();
        // 设置查询条件
        sq.set("q", "product_name:青蛙");
        // 设置过滤条件
        sq.set("fq", "product_price: [10 TO 100]");
        // 添加排序
        //sq.addSort("product_price", SolrQuery.ORDER.asc);
        sq.addSort("product_price", SolrQuery.ORDER.desc);
        // 分页查询
        sq.setStart(0);
        // 设置每一页的记录数
        sq.setRows(10);
        // 查询
        QueryResponse query = client.query(sq);
        // 获得查询结果
        SolrDocumentList results = query.getResults();
        // 获得查询的记录数
        long numFound = results.getNumFound();
        System.out.println("记录数：" + numFound);
        for (SolrDocument sd : results) {
            // 获得文档域
            String id = (String) sd.getFieldValue("id");
            String product_catalog_name = (String) sd.getFieldValue("product_catalog_name");
            Double product_price = (Double) sd.getFieldValue("product_price");
            String product_name = (String) sd.getFieldValue("product_name");
            String product_picture = (String) sd.getFieldValue("product_picture");
            System.out.println("id" + id);
            System.out.println("描述" + product_catalog_name);
            System.out.println("单价" + product_price);
            System.out.println("商品名称" + product_name);
            System.out.println("商品图片" + product_picture);
            System.out.println("------------------------");
        }
    }

    public static void queryFl() throws IOException, SolrServerException {
        String solrUrl = "http://localhost:8983/solr/demoInstance";
        //创建solr客户端对象
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(6000).build();
        // 创建solr查询对象
        SolrQuery sq = new SolrQuery();
        // 设置查询条件
        sq.set("q", "product_name:青蛙 AND product_keywords:吸盘");
        // 开启高亮（也可对多个域进行高亮）
        sq.setHighlight(true);
        sq.addHighlightField("product_name");
        sq.setHighlightSimplePre("<h1>");
        sq.setHighlightSimplePost("</h2>");
        // 查询
        QueryResponse query = client.query(sq);
        // 获得查询结果
        SolrDocumentList results = query.getResults();
        // 获得查询的记录数
        long numFound = results.getNumFound();
        System.out.println("记录数：" + numFound);
        for (SolrDocument sd : results) {
            // 获得文档域
            String id = (String) sd.getFieldValue("id");
            String product_catalog_name = (String) sd.getFieldValue("product_catalog_name");
            Double product_price = (Double) sd.getFieldValue("product_price");
            String product_name = (String) sd.getFieldValue("product_name");
            String product_picture = (String) sd.getFieldValue("product_picture");
            //System.out.println("id" + id);
            //System.out.println("描述" + product_catalog_name);
            //System.out.println("单价" + product_price);
            //System.out.println("商品名称" + product_name);
            //System.out.println("商品图片" + product_picture);
            System.out.println("------------------------");

            // 获得高亮的结构体
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            if (highlighting != null) {
                // 根据id获取每一个域的内容
                Map<String, List<String>> map = highlighting.get(id);
                // 根据具体的域来获得高亮内容（多个高亮域的获取下方代码复制换掉域名称即可）
                List<String> list = map.get("product_name");
                if (list != null && list.size() > 0) {
                    // 打印高亮内容
                    for (String s : list) {
                        System.out.println("高亮内容" + s);
                    }
                }
            }
        }
    }
}


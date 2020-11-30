package us.supercheng.es;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;
import us.supercheng.es.pojo.Stu;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    ElasticsearchTemplate template;


    @Test
    public void createIdxStu() {
        Stu stu = new Stu();

        stu.setName("Moka");
        stu.setAge(2);
        stu.setStuId(1L);
        stu.setMoney(19.8f);
        stu.setSign("Moka Pot");
        stu.setDesc("I am a Moka Pot");

        IndexQuery query = new IndexQueryBuilder()
                .withObject(stu).build();

        this.template.index(query);
    }

    @Test
    public void deleteIdxStu() {
        this.template.deleteIndex(Stu.class);
    }

    @Test
    public void updateStuDoc() {
        Map<String, Object> map = new HashMap<>();

        map.put("sign", "Espresso is good");
        map.put("money", 66.6f);


        IndexRequest request = new IndexRequest();
        request.source(map);


        UpdateQuery updateQuery =
                new UpdateQueryBuilder()
                        .withId("1")
                        .withIndexRequest(request)
                        .withClass(Stu.class)
                        .build();


        this.template.update(updateQuery);
    }

    @Test
    public void getStuDocById() {
        GetQuery query = new GetQuery();
        query.setId("1");
        Stu stu = this.template.queryForObject(query, Stu.class);
        System.out.println(stu);
    }

    @Test
    public void deleteStuDocById() {
        this.template.delete(Stu.class, "1");
    }

}

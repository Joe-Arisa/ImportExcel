package com.example.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.example.domain.User;
import com.example.mapper.DaoMapper;
import com.example.utils.UUIDUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UserDataListener extends AnalysisEventListener<User> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private List<User> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private int times = 0;

    @Override
    public void invoke(User data, AnalysisContext context) {

        data.setUserId(UUIDUtils.generateUUIDWithoutHyphens());

        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            try {
                saveData(cachedDataList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        try {
            saveData(cachedDataList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("|所有数据解析完成");
    }

    /**
     * 存储数据库
     */
    private void saveData(List<User> dataList) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DaoMapper mapper = sqlSession.getMapper(DaoMapper.class);
            mapper.insertUserData(dataList);
            sqlSession.commit(); // 提交事务，此时会执行批处理
            sqlSession.clearCache(); // 清除一级和二级缓存（如果需要）
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        times++;
        System.out.println("{" + times + "}次存储数据库！");
    }
}

package com.test.csvtodb.processor;

import com.test.csvtodb.model.MyEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CsvItemProcessor implements ItemProcessor<MyEntity, MyEntity> {

    @Override
    public MyEntity process(MyEntity item) throws Exception {
        // Perform any processing if needed
        return item;
    }
}
package com.avaje.ebeanservice.docstore.none;

import com.avaje.ebean.plugin.BeanType;
import com.avaje.ebeanservice.docstore.api.DocStoreQueryUpdate;
import com.avaje.ebeanservice.docstore.api.DocStoreUpdateProcessor;
import com.avaje.ebeanservice.docstore.api.DocStoreUpdates;

import java.io.IOException;

/**
 * DocStoreUpdateProcessor that barfs if it is used.
 */
public class NoneDocStoreUpdateProcessor implements DocStoreUpdateProcessor {

  @Override
  public <T> DocStoreQueryUpdate<T> createQueryUpdate(BeanType<T> beanType, int bulkBatchSize) throws IOException {
    throw NoneDocStore.implementationNotInClassPath();
  }

  @Override
  public void process(DocStoreUpdates docStoreUpdates, int bulkBatchSize) {
    throw NoneDocStore.implementationNotInClassPath();
  }
}

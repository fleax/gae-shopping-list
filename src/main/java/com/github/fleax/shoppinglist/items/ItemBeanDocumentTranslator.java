package com.github.fleax.shoppinglist.items;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;

public class ItemBeanDocumentTranslator {

	public ItemBean documentToBean(Document document) {
		ItemBean itemBean = new ItemBean();
		itemBean.setId(document.getId());
		itemBean.setName(document.getOnlyField("original").getText());
		return itemBean;
	}

	public Document beanToDocument(ItemBean item) {
		Document.Builder builder = Document.newBuilder();
		builder.addField(Field.newBuilder().setName("original")
				.setText(item.getName()).build());

		for (int i = 2; i < item.getName().length(); i++) {
			builder.addField(Field.newBuilder().setName("tokens")
					.setText(item.getName().subSequence(0, i).toString())
					.build());
		}
		return builder.build();
	}

}

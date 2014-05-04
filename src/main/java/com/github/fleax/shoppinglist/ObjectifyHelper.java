package com.github.fleax.shoppinglist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

/**
 * Objectify Utilities
 * 
 * @author fleax
 * 
 */
public class ObjectifyHelper {

    /**
     * @param bean
     * @return key of saved object
     */
    public static <T> Key<T> save(T bean) {
	return ObjectifyService.ofy().save().entity(bean).now();
    }

    /**
     * @param ancestor
     * @param type
     * @return get object by type and id
     */
    public static <T> T get(Object ancestor, Class<T> type, Long id) {
	if (ancestor == null) {
	    return ObjectifyService.ofy().load().type(type).id(id).getValue();
	} else {
	    return ObjectifyService.ofy().load().type(type).parent(ancestor)
		    .id(id).getValue();
	}
    }

    /**
     * @param type
     * @return get object by type and id
     */
    public static <T> T get(Class<T> type, String id) {
	return ObjectifyService.ofy().load().type(type).id(id).getValue();
    }

    /**
     * @param ancestor
     * @param type
     * @return list of objects of given type
     */
    public static <T> List<T> list(Object ancestor, Class<T> type) {
	if (ancestor == null) {
	    return ObjectifyService.ofy().load().type(type).list();
	} else {
	    return ObjectifyService.ofy().load().type(type).ancestor(ancestor)
		    .list();
	}
    }

    /**
     * @param ancestor
     * @param type
     * @param filter
     * @param value
     * @return list of objects of given type, applying filter
     */
    public static <T> List<T> list(Object ancestor, Class<T> type,
	    String filter, Object value) {
	if (ancestor == null) {
	    return ObjectifyService.ofy().load().type(type)
		    .filter(filter, value).list();
	} else {
	    return ObjectifyService.ofy().load().type(type).ancestor(ancestor)
		    .filter(filter, value).list();
	}
    }

    /**
     * Transforms a list of references to a list of objects
     * 
     * @param listOfReferences
     * @return list of objects retrieved
     */
    public static <T> List<T> resolveReferences(
	    Collection<Ref<T>> listOfReferences) {
	List<T> list = new ArrayList<T>();
	if (listOfReferences != null) {
	    for (Ref<T> ref : listOfReferences) {
		T value = resolveReference(ref);
		if (value != null) {
		    list.add(value);
		}
	    }
	}
	return list;
    }

    /**
     * Returns object given a reference
     * 
     * @param reference
     * @return object referenced
     */
    public static <T> T resolveReference(Ref<T> reference) {
	if (reference != null) {
	    return reference.getValue();
	}
	return null;
    }

    /**
     * Transforms a list of objects in a list of references
     * 
     * @param listOfObjects
     * @return list of references
     */
    public static <T> List<Ref<T>> createReferences(List<T> listOfObjects) {
	List<Ref<T>> listOfReferences = new ArrayList<Ref<T>>();
	if (listOfObjects != null) {
	    for (T object : listOfObjects) {
		Ref<T> ref = createReference(object);
		if (ref != null) {
		    listOfReferences.add(ref);
		}
	    }
	}
	return listOfReferences;
    }

    /**
     * Creates a reference to an object
     * 
     * @param object
     * @return reference
     */
    public static <T> Ref<T> createReference(T object) {
	if (object != null) {
	    return Ref.create(object);
	}
	return null;
    }
}

package org.smartparam.engine.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class PackageListTest {

    private PackageList packageList = null;

    @Before
    public void setup() {
        packageList = new PackageList();
    }

    @Test
    public void testSetAndGetPackages() {
        List<String> packages = new ArrayList<String>();

        packageList.setPackages(packages);

        assertTrue(CollectionUtils.isEqualCollection(packages, packageList.getPackages()));
    }

    @Test
    public void testGetPackages_withoutSet() {
        assertTrue(packageList.getPackages().isEmpty());
    }

    @Test
    public void testIterate() {
        List<String> packages = new ArrayList<String>();
        packages.add("first");
        packages.add("second");

        packageList.setPackages(packages);

        int i = 0;
        for (String packageName : packageList) {
            assertEquals(packageName, packages.get(i));
            i++;
        }
    }

    @Test
    public void testIterate_withoutSet() {
        Iterator<String> iterator = packageList.iterator();
        assertFalse(iterator.hasNext());
    }
}

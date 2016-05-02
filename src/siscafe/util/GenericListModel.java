
package siscafe.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author jecheverri
 * @param <String>
 */
public class GenericListModel <String> extends AbstractListModel {
    
    public GenericListModel() {
        this.listObject = new ArrayList();
    }

    @Override
    public int getSize() {
        return this.listObject.size();
    }

    @Override
    public String getElementAt(int index) {
        return this.listObject.get(index);
    }
    
    public void add(String node) {
        this.listObject.add(node);
    }

    private List<String> listObject;
}

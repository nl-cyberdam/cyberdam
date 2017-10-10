package nl.cyberdam.web;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.support.PagedListSourceProvider;

/**
 *
 */
public class SwfDirectoryController extends GameListController {
	
	private String swfPath;
    
	public class swfParam {
		public swfParam(String name, Date date) {
			this.date = date;
			this.name = name;
		}
		private String name;
		private Date date;
		public String getName() {
			return name;
		}
		public Date getDate() {
			return date;
		}
	}
	
    /** Creates a new instance of SwfDirectoryController */
    public SwfDirectoryController() {
    	super("swfs", "swf");
    	setListSourceProvider(new SwfListProvider());
    	
    }
    
    private class SwfListProvider implements PagedListSourceProvider, Serializable {

        public List<SwfDirectoryController.swfParam> loadList(Locale loc, Object filter) {
          	List<SwfDirectoryController.swfParam> list = new ArrayList<SwfDirectoryController.swfParam>();
        	list.addAll(getFileList(swfPath));
        	return list;
        }
        
        private List<SwfDirectoryController.swfParam> getFileList(String path) {
        	List<SwfDirectoryController.swfParam> list = new ArrayList<SwfDirectoryController.swfParam>();
        	File dir = new File(getServletContext().getRealPath(path));
            String[] children = dir.list();
            if (children != null) {
                for (int i=0; i<children.length; i++) {
                    File file = new File(getServletContext().getRealPath(path), children[i]);
                    list.add(new swfParam(children[i], new Date(file.lastModified())));
                }
            }
            return list;
        }
    }
    
    public void setSwfPath(String swfPath) {
		logger.info("setSwfPath: " + swfPath);
		this.swfPath = swfPath;
	}
}

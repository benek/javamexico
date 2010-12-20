package org.javamexico.site.pages.blogs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.BlogDao;
import org.javamexico.entity.blog.BlogPost;
import org.javamexico.entity.blog.TagBlog;
import org.javamexico.site.base.Pagina;

@Import(stylesheet="context:layout/preguntas.css")
public class Tag extends Pagina {

	@Property private Set<TagBlog> tags;
	@SuppressWarnings("unused")
	@Property private BlogPost blog;
	@SuppressWarnings("unused")
	@Property private TagBlog tag;
	@Property private TagBlog elTag;
	@Inject @Service("blogDao")
	private BlogDao bdao;

	Object onActivate(String _tags) {
		String[] ts = _tags.split(" ");
		tags = new HashSet<TagBlog>(ts.length);
		if (ts.length == 1) {
			elTag = bdao.findTag(ts[0]);
			if (elTag != null) {
				tags.add(elTag);
			}
		} else {
			tags.addAll(bdao.findTags(ts));
		}
		return tags.size() == 0 ? Index.class : null;
	}

	public boolean isUno() {
		return tags != null && tags.size() == 1;
	}

	public List<BlogPost> getBlogs() {
		if (elTag == null) {
			return bdao.getBlogsConTags(tags);
		} else {
			return bdao.getBlogsConTag(elTag);
		}
	}

	public List<TagBlog> getPopTags() {
		return bdao.getTagsPopulares(10);
	}

}

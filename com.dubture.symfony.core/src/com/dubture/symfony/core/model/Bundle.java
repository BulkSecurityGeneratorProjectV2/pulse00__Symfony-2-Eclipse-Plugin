/*******************************************************************************
 * This file is part of the Symfony eclipse plugin.
 * 
 * (c) Robert Gruendler <r.gruendler@gmail.com>
 * 
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.dubture.symfony.core.model;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.INamespace;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.SourceNamespace;
import org.eclipse.dltk.internal.core.SourceType;
import org.eclipse.dltk.internal.core.hierarchy.FakeType;


@SuppressWarnings("restriction")
public class Bundle extends SourceType {

	public static final String NAME = "name";	
	public static final String NAMESPACE = "namespace";
	public static final String PATH = "path";
	private SourceNamespace _namespace = null;
	
	private String _path = null;
	
	private IScriptProject _project;
	
	
	public Bundle(ModelElement parent, String name) {
		super(parent, name);
		
	}
	
	public void setPath(String path) {
		
		_path = path;
		
	}
	

	@Override
	public IPath getPath() {

		IPath path = null;
		
		try {
			path = super.getPath();
		} catch (Exception e) {
			
			path = new Path(_path);
			
		}		
		
		return path;
	}
	
	@Override
	public int getFlags() throws ModelException {

		return Modifiers.AccPublic;
	}
	
	@Override
	public Object getElementInfo() throws ModelException {

		return new FakeTypeElementInfo();
	}
	
	@Override
	protected Object openWhenClosed(Object info, IProgressMonitor monitor)
			throws ModelException {

		return new FakeTypeElementInfo();

	}
	
	@Override
	public ISourceModule getSourceModule() {
	
		return super.getSourceModule();
	}
	
	@Override
	public IModelElement getParent() {
	
		// avoid showing the same name twice in each codeassist
		// popup row, ie:
		// AcmeDemoBundle - AcmeDemoBundle
		return new FakeType(parent, "");		
		
	}

	/**
	 * Returns the bundle namespace or null
	 */
	public INamespace getNamespace() throws ModelException {

		if (_namespace != null)
			return _namespace;
		
		String path = getPath().toString();
		
		if (path == null)
			return super.getNamespace();
		
		String[] parts = StringUtils.splitByCharacterTypeCamelCase(getElementName());

		if (parts.length == 0) {
			return super.getNamespace();
		}

		String first = parts[0];
		int index = path.indexOf(first);
		
		if (index == -1)
			return super.getNamespace();

		
		String ns = path.substring(index);
		String[] namespace = ns.split("/");
		_namespace = new SourceNamespace(namespace);  
 		
		return _namespace;
		
	}	
	
	public String getTranslationPath() {
		
		return _path + "/Resources/translations";
		
	}
	
	@Override
	public IScriptProject getScriptProject() {

		if (_project != null)
			return _project;

		return super.getScriptProject();
	}

	public void setProject(IScriptProject project) {
		
		_project = project;
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.dltk.internal.core.SourceType#getElementType()
	 */
	@Override
	public int getElementType()
	{
	    return ISymfonyModelElement.BUNDLE;
	}
	
	
}

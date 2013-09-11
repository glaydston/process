package com.ibm.processos;

import java.io.*;
import java.util.List;

import javax.portlet.*;

import model.Member;
import model.Process;
import dao.MemberDao;
import dao.ProcessDao;

/**
 * A sample portlet based on GenericPortlet
 */
public class ProcessosPortlet extends GenericPortlet {
	/**
	 * @see javax.portlet.Portlet#init()
	 */
	public void init() throws PortletException {
		super.init();
	}

	/**
	 * Serve up the <code>view</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		// Set the MIME type for the render response
		response.setContentType(request.getResponseContentType());

		String targetJsp = chooseViewTask(request);
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(
				targetJsp);
		rd.include(request, response);
	}

	private String chooseViewTask(RenderRequest request) {
		MemberDao mdao = new MemberDao();
		List<Member> members = mdao.getMembers();
		request.setAttribute("members", members);
		request.setAttribute("message", "");
		return "/jsp/ProcessosPortletView.jsp";
	}

	/**
	 * Serve up the <code>edit</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doEdit(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		// TODO: auto-generated method stub
	}

	/**
	 * Serve up the <code>help</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doHelp(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	protected void doHelp(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		// TODO: auto-generated method stub
	}

	/**
	 * Process an action request.
	 * 
	 * @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, java.io.IOException {

		String name = request.getParameter("name");
		String description = request.getParameter("description");
		long member = Long.parseLong(request.getParameter("value"));

		Process p = new Process();
		
		System.out.println("#PNAME: " + name);
		System.out.println("#PDESCRIPTION: " + description);
		System.out.println("#PMEMBER: " + member);

		p.setName(name);
		p.setDescription(description);
		p.setMember(new MemberDao().getMember(member));
		

		String message = new ProcessDao().setProcess(p);

		request.setAttribute("message", message);
	}

}

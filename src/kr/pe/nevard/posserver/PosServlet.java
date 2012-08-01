package kr.pe.nevard.posserver;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.ac.kaist.swrc.jhannanum.comm.Eojeol;
import kr.ac.kaist.swrc.jhannanum.comm.Sentence;
import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;

/**
 * Servlet implementation class PosServlet
 */
@WebServlet("/pos")
public class PosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.getRequestDispatcher("/form.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Workflow workflow = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_NOUN_EXTRACTOR);
		PrintStream out = new PrintStream(response.getOutputStream());
		
		try
		{
			/* Activate the work flow in the thread mode */
			workflow.activateWorkflow(true);
			
			/* Analysis using the work flow */
			String document = request.getParameter("sentence");
			workflow.analyze(document);
			
			LinkedList<Sentence> resultList = workflow.getResultOfDocument(new Sentence(0, 0, false));
			for (Sentence s : resultList) {
				Eojeol[] eojeolArray = s.getEojeols();
				for (int i = 0; i < eojeolArray.length; i++) {
					if (eojeolArray[i].length > 0) {
						String[] morphemes = eojeolArray[i].getMorphemes();
						for (int j = 0; j < morphemes.length; j++) {
							out.print(morphemes[j]);
						}
						out.print(", ");
					}
				}
			}
		}
		catch(Exception e)
		{
			out.println("<xmp>");
			e.printStackTrace(out);
			out.println("</xmp>");
		}
		out.flush();
			
		// Shutdown the work flow
		workflow.close();		
	}
}

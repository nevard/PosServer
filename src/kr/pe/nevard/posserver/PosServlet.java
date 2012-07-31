package kr.pe.nevard.posserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		Workflow workflow1 = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_HMM_POS_TAGGER);
		Workflow workflow2 = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_POS_SIMPLE_09);
		
		try {
			// Activate the work flow in the thread mode
			workflow1.activateWorkflow(true);
			workflow2.activateWorkflow(true);
			
			// Analysis using the work flow
			String sentence = "학교에서조차도 그 사실을 모르고 있었다.";
			workflow1.analyze(sentence);
			workflow2.analyze(sentence);

			PrintWriter out = new PrintWriter(response.getOutputStream());
			out.println("<pre>");
			out.println("# POS tagging result with 69 tags.\n");
			out.println(workflow1.getResultOfSentence());
			
			out.println("# POS tagging result with 9 tags.\n");
			out.println(workflow2.getResultOfSentence());
			out.flush();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Shutdown the work flow
		workflow1.close();  	
		workflow2.close();
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

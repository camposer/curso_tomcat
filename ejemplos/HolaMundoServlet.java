@WebServlet("/HolaMundo")
public class HolaMundoServlet extends HttpServlet {
		public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("Hola mundo!<br/>");
			response.getWriter().println("La fecha de hoy es: ");
			response.getWriter().println(new java.util.Date());
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
		}
}
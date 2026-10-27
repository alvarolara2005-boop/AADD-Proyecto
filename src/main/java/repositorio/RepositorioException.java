package repositorio;

public class RepositorioException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RepositorioException(String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

	public RepositorioException(String mensaje) {
		super(mensaje);
	}
}

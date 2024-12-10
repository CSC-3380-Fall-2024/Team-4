import './Login.css';

const Register = () => {
    return (
        <>
            <div className="container">
                <div className="screen">
                    <div className="screen__content">
                        <form className="login">
                            <div className="login__field">
                                <input type="text" className="login__input" placeholder="Username"/>
                            </div>
                            <div className="login__field">
                                <input type="password" className="login__input" placeholder="Password"/>
                            </div>
                            <button className="button login__submit">
                                <span className="button__text">Register</span>
                            </button>
                            <a className="switch__login" href="">Login</a>
                        </form>
                    </div>
                    <div className="screen__background">
                        <span className="screen__background__shape screen__background__shape4"></span>
                        <span className="screen__background__shape screen__background__shape3"></span>		
                        <span className="screen__background__shape screen__background__shape2"></span>
                        <span className="screen__background__shape screen__background__shape1"></span>
                    </div>		
                </div>
            </div>
        </>
    )
}

export default Register;
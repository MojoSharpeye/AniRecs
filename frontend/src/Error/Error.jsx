import styles from "./Error.module.css";
import Fred from "../assets/MAL/fred.jpg";

function Error() {
  return (
    <div className={styles.error}>
      <h1>Error!</h1>
      <img className={styles.img} src={Fred} alt="error image"></img>
      <h3>Have you checked:</h3>
      <div className={styles.container}>
        <li className={styles.list}>
          your account has scored at least 1 anime
        </li>
        <li className={styles.list}>your account name is spelled correctly</li>
        <li className={styles.list}>
          your account's anime list is set to public
        </li>
      </div>
    </div>
  );
}
export default Error;

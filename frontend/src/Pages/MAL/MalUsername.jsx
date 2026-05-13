import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./MalUsername.module.css";

function MalUsername() {
  const [username, setUsername] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!username.trim()) return;

    navigate(`/${username}/profile`);
  };

  return (
    <div className={styles.malUsername}>
      <form className={styles.formBox} onSubmit={handleSubmit}>
        <input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className={styles.input}
          placeholder="Enter your MAL username"
        />

        <button
          type="submit"
          className={`${styles.button} ${username ? styles.show : ""}`}
          disabled={!username}
        >
          Analyse User
        </button>
      </form>
    </div>
  );
}

export default MalUsername;

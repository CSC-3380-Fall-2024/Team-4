import React from "react";
import styles from "./Nav.module.css";
import data from "./links.json";

interface Link {
  href: string;
  label: string;
}

interface LinksProps {
  links: Link[];
}

const logo = "/icons/logo.png";

const Links: React.FC<LinksProps> = ({ links }) => {
  return (
    <div className={styles["links-container"]}>
      {links.map((link) => (
        <div key={link.href} className={styles["link"]}>
          <a href={link.href}>{link.label}</a>
        </div>
      ))}
    </div>
  );
};

const Nav: React.FC = () => {
  const links: Link[] = data.links;

  return (
    <nav className={styles.navbar}>
      <div className={styles["logo-container"]}>
        <img src={logo} alt="logo" className={styles.logo} />
      </div>
      <Links links={links} />
    </nav>
  );
};

export default Nav;

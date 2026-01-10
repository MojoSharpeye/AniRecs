export default function DecorativeIframeBackground({
  src = "/Decorative/index2.html",
  zIndex = -1,
}) {
  return (
    <iframe
      src={src}
      title="Decorative background"
      style={{
        position: "fixed",
        width: "100vw",
        height: "100vh",
        border: "none",
        zIndex: zIndex,
      }}
      aria-hidden="true"
    />
  );
}

export default function DecorativeIframeBackground({
  src = "/Decorative/index2.html",
  zIndex = -1,
  // pointerEvents = "auto"
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
        // inset: 0,           // top:0 right:0 bottom:0 left:0
        // margin: 0,
        // padding: 0,
        // overflow: "hidden",
        // pointerEvents: pointerEvents, // "none" lets clicks pass through
        // background: "transparent"
      }}
      aria-hidden="true"
    />
  );
}
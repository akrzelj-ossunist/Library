const Modal: React.FC<{ children: JSX.Element }> = ({ children }) => {
  return (
    <div className="w-[100vw] h-[100vh] top-0 absolute z-50 bg-[rgba(0,0,0,0.4)] flex justify-center items-center">
      {children}
    </div>
  );
};

export default Modal;

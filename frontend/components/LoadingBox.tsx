import Spinner from 'react-bootstrap/Spinner';

export default function LoadingBox() {
  return (
    <div className="justify-center align-middle text-center">
      <Spinner animation="border" role="status">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    </div>
  );
}

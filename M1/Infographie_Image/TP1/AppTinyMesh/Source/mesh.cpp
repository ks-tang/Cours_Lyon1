#include "mesh.h"

/*!
\class Mesh mesh.h

\brief Core triangle mesh class.
*/



/*!
\brief Initialize the mesh to empty.
*/
Mesh::Mesh()
{
}

/*!
\brief Initialize the mesh from a list of vertices and a list of triangles.

Indices must have a size multiple of three (three for triangle vertices and three for triangle normals).

\param vertices List of geometry vertices.
\param indices List of indices wich represent the geometry triangles.
*/
Mesh::Mesh(const std::vector<Vector>& vertices, const std::vector<int>& indices) :vertices(vertices), varray(indices)
{
  normals.resize(vertices.size(), Vector::Z);
}

/*!
\brief Create the mesh.

\param vertices Array of vertices.
\param normals Array of normals.
\param va, na Array of vertex and normal indexes.
*/
Mesh::Mesh(const std::vector<Vector>& vertices, const std::vector<Vector>& normals, const std::vector<int>& va, const std::vector<int>& na) :vertices(vertices), normals(normals), varray(va), narray(na)
{
}

/*!
\brief Reserve memory for arrays.
\param nv,nn,nvi,nvn Number of vertices, normals, vertex indexes and vertex normals.
*/
void Mesh::Reserve(int nv, int nn, int nvi, int nvn)
{
  vertices.reserve(nv);
  normals.reserve(nn);
  varray.reserve(nvi);
  narray.reserve(nvn);
}

/*!
\brief Empty
*/
Mesh::~Mesh()
{
}

/*!
\brief Smooth the normals of the mesh.

This function weights the normals of the faces by their corresponding area.
\sa Triangle::AreaNormal()
*/
void Mesh::SmoothNormals()
{
  // Initialize 
  normals.resize(vertices.size(), Vector::Null);

  narray = varray;

  // Accumulate normals
  for (int i = 0; i < varray.size(); i += 3)
  {
    Vector tn = Triangle(vertices[varray.at(i)], vertices[varray.at(i + 1)], vertices[varray.at(i + 2)]).AreaNormal();
    normals[narray[i + 0]] += tn;
    normals[narray[i + 1]] += tn;
    normals[narray[i + 2]] += tn;
  }

  // Normalize 
  for (int i = 0; i < normals.size(); i++)
  {
    Normalize(normals[i]);
  }
}

/*!
\brief Add a smooth triangle to the geometry.
\param a, b, c Index of the vertices.
\param na, nb, nc Index of the normals.
*/
void Mesh::AddSmoothTriangle(int a, int na, int b, int nb, int c, int nc)
{
  varray.push_back(a);
  narray.push_back(na);
  varray.push_back(b);
  narray.push_back(nb);
  varray.push_back(c);
  narray.push_back(nc);
}

/*!
\brief Add a triangle to the geometry.
\param a, b, c Index of the vertices.
\param n Index of the normal.
*/
void Mesh::AddTriangle(int a, int b, int c, int n)
{
  varray.push_back(a);
  narray.push_back(n);
  varray.push_back(b);
  narray.push_back(n);
  varray.push_back(c);
  narray.push_back(n);
}

/*!
\brief Add a smmoth quadrangle to the geometry.

Creates two smooth triangles abc and acd.

\param a, b, c, d  Index of the vertices.
\param na, nb, nc, nd Index of the normal for all vertices.
*/
void Mesh::AddSmoothQuadrangle(int a, int na, int b, int nb, int c, int nc, int d, int nd)
{
  // First triangle
  AddSmoothTriangle(a, na, b, nb, c, nc);

  // Second triangle
  AddSmoothTriangle(a, na, c, nc, d, nd);
}

/*!
\brief Add a quadrangle to the geometry.

\param a, b, c, d  Index of the vertices and normals.
*/
void Mesh::AddQuadrangle(int a, int b, int c, int d)
{
  AddSmoothQuadrangle(a, a, b, b, c, c, d, d);
}

/*!
\brief Compute the bounding box of the object.
*/
Box Mesh::GetBox() const
{
  if (vertices.size() == 0)
  {
    return Box::Null;
  }
  return Box(vertices);
}

/*!
\brief Creates an axis aligned box.

The object has 8 vertices, 6 normals and 12 triangles.
\param box The box.
*/
Mesh::Mesh(const Box& box)
{
  // Vertices
  vertices.resize(8);

  for (int i = 0; i < 8; i++)
  {
    vertices[i] = box.Vertex(i);
  }

  // Normals
  normals.push_back(Vector(-1, 0, 0));
  normals.push_back(Vector(1, 0, 0));
  normals.push_back(Vector(0, -1, 0));
  normals.push_back(Vector(0, 1, 0));
  normals.push_back(Vector(0, 0, -1));
  normals.push_back(Vector(0, 0, 1));

  // Reserve space for the triangle array
  varray.reserve(12 * 3);
  narray.reserve(12 * 3);

  AddTriangle(0, 2, 1, 4);
  AddTriangle(1, 2, 3, 0);

  AddTriangle(4, 5, 6, 5);
  AddTriangle(5, 7, 6, 5);

  AddTriangle(0, 4, 2, 0);
  AddTriangle(4, 6, 2, 0);

  AddTriangle(1, 3, 5, 1);
  AddTriangle(3, 7, 5, 1);

  AddTriangle(0, 1, 5, 2);
  AddTriangle(0, 5, 4, 2);

  AddTriangle(3, 2, 7, 3);
  AddTriangle(6, 7, 2, 3);
}

/*!
\brief Creates a circle
The object has 100 vertices and 99 triangles.
\param c The circle.
*/
Mesh::Mesh(const Circle &c) {

  const int div = c.getNbDiv();
  //vertices
  vertices.resize(div);
  float alpha;
  float step = 2.0*M_PI / (div-2);
  Vector center = c.Center();
  
  vertices[div-1] = center;
  for (int i = 0; i < div-1; i++)
  {
    alpha = i*step;
    vertices[i] = Vector(c.Radius()*cos(alpha)+center[0], center[1], c.Radius()*sin(alpha + center[2]));
  }

  normals.push_back(Vector(0,1,0));
  // Reserve space for the triangle array
  varray.reserve((div-1) * 3);
  narray.reserve((div-1) * 3);

  for(int i=0; i<div-1; i++){
    AddTriangle(div-1, i, i+1, 0);
  }
}

/*!
\brief Creates a cylinder
The object is composed of 2 connected circles
\param c The cylinder.
*/
Mesh::Mesh(const Cylinder &c) {

  const int div = c.getNbDiv();

  //vertices
  vertices.resize(div*2);
  float alpha;
  float step = 2.0*M_PI / (div-2);
  Vector center = c.Center();
  
  vertices[div-1] = center;
  vertices[2*div-1] = Vector(center[0], c.getLength() + center[1], center[2]);
  for (int i = 0; i < div-1; i++)
  {
    alpha = i*step;
    vertices[i] = Vector(cos(alpha)+center[0], center[1], sin(alpha) + center[2]);
    normals.push_back(Vector(cos(alpha), 0, sin(alpha)));
  }

  for (int j = div; j < 2*div-1; j++)
  {
    alpha = (j-div)*step;
    vertices[j] = Vector(cos(alpha)+center[0], c.getLength()+center[1], sin(alpha)+center[2]);
    normals.push_back(Vector(cos(alpha), 0, sin(alpha)));
  }
  normals.push_back(Vector(1,0,0));
  // Reserve space for the triangle array
  varray.reserve((2*div-1) * 3);
  narray.reserve((2*div-1) * 3);

  for(int i=0; i<div-1; i++){
    AddTriangle(div-1, i, i+1, 0);
  }
  for(int j=div; j<2*div-1; j++) {
    AddTriangle(2*div-1, j , j+1, 1);
  }
  for(int k=0; k<div-1; k++) {
      //AddTriangle(k, k+div, k+1, 2);
      AddSmoothTriangle(k, k, k+div, k+div, k+1, k+1);
      //AddTriangle(k+div, k+1, k+div+1, 3);
      AddSmoothTriangle(k+div, k+div, k+1, k+1, k+div+1, k+div+1);
  }
}

/*!
\brief Creates a sphere
The object is composed of multiples connected circles
\param s The sphere.
*/
Mesh::Mesh(const Sphere &s) {
  float x,y,z;
  float xy, nx, ny, nz;
  float lengthInv = 1.0/s.getRadius();
  float pasH = 2*M_PI/s.getNbHorizontal();
  float pasV = M_PI/s.getNbVertical();
  float angleH;
  float angleV;
  for(int i=0; i<=s.getNbVertical(); i++){
    angleV = M_PI / 2 - i * pasV;
    xy = s.getRadius()*cosf(angleV);
    z = s.getRadius()*sinf(angleV) + s.getCenter()[2];

    for(int j=0; j<=s.getNbHorizontal(); j++) {
      angleH = j * pasH;

      x = xy * cosf(angleH) + s.getCenter()[0];
      y = xy * sinf(angleH) + s.getCenter()[1];
      vertices.push_back(Vector(x,y,z));

      nx = x * lengthInv;
      ny = y * lengthInv;
      nz = z * lengthInv;
      normals.push_back(Vector(nx,ny,nz));
    }
  }
  int indActuel;
  int indFutur;
  varray.reserve((s.getNbHorizontal() * s.getNbVertical())* 2 * 3);
  for(int k=0; k<s.getNbVertical(); k++) {
    indActuel = k * (s.getNbHorizontal() + 1);
    indFutur = indActuel + s.getNbHorizontal() + 1;

    for(int l=0; l<s.getNbHorizontal(); l++) {
      if(k != 0) {
        AddSmoothTriangle(indActuel, indActuel, indFutur, indFutur, indActuel+1, indActuel+1);
      }
      if(k != (s.getNbVertical()-1)) {
        AddSmoothTriangle(indActuel+1, indActuel+1, indFutur, indFutur, indFutur+1, indFutur+1);
      }
      indActuel++;
      indFutur++;
    }
  }
}

/*!
\brief Creates a demi-sphere
\param d The demi-sphere.
*/
Mesh::Mesh(const Demisphere &d) {
  float x,y,z;
  float xy, nx, ny, nz;
  float lengthInv = 1.0/d.getRadius();
  float pasH = M_PI/d.getNbHorizontal();
  float pasV = M_PI/d.getNbVertical();
  float angleH;
  float angleV;
  for(int i=0; i<=d.getNbVertical(); i++){
    angleV = M_PI / 2 - i * pasV;
    xy = d.getRadius()*cosf(angleV);
    z = d.getRadius()*sinf(angleV) + d.getCenter()[2];

    for(int j=0; j<=d.getNbHorizontal(); j++) {
      angleH = j * pasH;

      x = xy * cosf(angleH) + d.getCenter()[0];
      y = xy * sinf(angleH) + d.getCenter()[1];
      vertices.push_back(Vector(x,y,z));

      nx = x * lengthInv;
      ny = y * lengthInv;
      nz = z * lengthInv;
      normals.push_back(Vector(nx,ny,nz));
    }
  }
  int indActuel;
  int indFutur;
  varray.reserve((d.getNbHorizontal() * d.getNbVertical())* 2 * 3);
  for(int k=0; k<d.getNbVertical(); k++) {
    indActuel = k * (d.getNbHorizontal() + 1);
    indFutur = indActuel + d.getNbHorizontal() + 1;

    for(int l=0; l<d.getNbHorizontal(); l++) {
      if(k != 0) {
        AddSmoothTriangle(indActuel, indActuel, indFutur, indFutur, indActuel+1, indActuel+1);
      }
      if(k != (d.getNbVertical()-1)) {
        AddSmoothTriangle(indActuel+1, indActuel+1, indFutur, indFutur, indFutur+1, indFutur+1);
      }
      indActuel++;
      indFutur++;
    }
  }
}

/*!
\brief Creates a torus
The object is composed of multiples connected circles builded around a virtual circle (skeleton)
\param t The torus.
*/
Mesh::Mesh(const Tore &t) {
  int nbCercle = t.getSkeleton().getNbDiv(); //nb de cercle dans le tore
  int nbDiv = t.getNbDiv(); 
  int innerR = t.getRadius(); //inner radius
  int outerR = t.getSkeleton().Radius();

  //ring radius(thick part of donut)
  float phi = 0.0;
  float dp = (2 * M_PI) / nbDiv;
  // torus radius(whole donut shape)
  float theta = 0.0;
  float dt = (2 * M_PI) / nbCercle;

  for (int i = 0; i <= nbCercle; i++)
  {
      theta = dt * i;
      for (int j = 0; j <= nbDiv; j++)
      {
          phi = dp * j;
          Vector v(cos(theta) * (outerR + cos(phi) * innerR), 
                  sin(theta) * (outerR + cos(phi) * innerR), 
                  sin(phi) * innerR);
          vertices.push_back(v);
          normals.push_back(v - Vector(0,0,0));
      }
  }
    
  varray.reserve(vertices.size() * 3);
  narray.reserve(vertices.size() * 3);

  for (int i = 0; i < vertices.size() - 1; ++i)
  {
      AddSmoothTriangle(i, i, (i + 1) % vertices.size(), (i + 1) % vertices.size(), 
        (i + nbDiv) % vertices.size(), (i + nbDiv) % vertices.size());
      AddSmoothTriangle((i + nbDiv) % vertices.size(), (i + nbDiv) % vertices.size(), 
        (i + 1 + nbDiv) % vertices.size(), (i + 1 + nbDiv) % vertices.size(), (i + 1) % vertices.size(), (i + 1) % vertices.size());
  }
}

Mesh::Mesh(Heightfield h){
  int width = h.getWidth();
  int height = h.getHeight();
  varray.reserve(width*height);
  narray.reserve(width*height);
  for(int i=0; i<height; i++){
    for(int j=0; j<width; j++) {
      vertices.push_back(Vector(i,j,h.getColor(i*width+j)));
    }
  }
  for(int i=0; i<height; i++){
    for(int j=0; j<width; j++) {
        Vector v1 = (vertices[j+1+width*i] - vertices[j+width*i]);
        Vector v2 = (vertices[width*(i+1)+j] - vertices[j+width*i]);

        Vector normal = Vector(v1[1]*v2[2]-v1[2]*v2[1], -v1[0]*v2[2]+v1[2]*v2[0], v1[0]*v2[1]-v1[1]*v2[0]);
        normals.push_back(normal);
    }
  }
  for(int i=0; i<height-1; i++){
    for(int j=0; j<width; j++) {
      if(j==0 | j%(width-1) !=0){
        AddSmoothTriangle(width*i+j, width*i+j, width*i+j+1, width*i+j+1, (i+1)*width+j, (i+1)*width+j);
        AddSmoothTriangle(width*i+j+1, width*i+j+1, (i+1)*width+j, (i+1)*width+j, (i+1)*width+j+1, (i+1)*width+j+1);
      }
    }
  }
}

/*!
\brief Rotate the mesh.
\param m Matrix of rotation.
*/
void Mesh::rotate(Matrix &m) {
  const int lengthVertex = this->vertices.size();
  const int lengthNormals = this->normals.size();
  for(int i=0; i<lengthVertex; i++) {
    this->vertices[i] = m*this->vertices[i];
  }
  for(int j=0; j<lengthNormals; j++) {
    this->normals[j] = m*this->normals[j];
  }
}

/*!
\brief Add a mesh to another one
\param m The mesh to add 
*/
void Mesh::merge(const Mesh &m) {
  const int lengthV = this->Vertexes();
  const int lengthN = this->Normals();
  for(int i=0; i<m.vertices.size(); i++){
    this->vertices.push_back(m.vertices[i]);  //Agrandissement de la liste des vertices avec ceux de la 2e forme
  }

  for(int j=0; j<m.normals.size(); j++) {
    this->normals.push_back(m.normals[j]);
  }

  for(int k=0;k<m.Triangles(); k++) {
    this->AddTriangle(m.VertexIndex(k,0)+lengthV, m.VertexIndex(k,1)+lengthV, m.VertexIndex(k,2)+lengthV, lengthN+m.NormalIndex(k,0));
  }
}

void Mesh::pointWarp(const Vector &v) {
  for(int i=0; i<this->vertices.size(); i++) {
    double dist = sqrt((pow(v[0]-this->vertices[i][0],2)) + (pow(v[1] - this->vertices[i][1], 2)) + (pow(v[2] - this->vertices[i][2], 2)));
    double distX = v[0] - this->vertices[i][0];
    double distY = v[1] - this->vertices[i][1];
    double distZ = v[2] - this->vertices[i][2];
    if(distX >= 1){
      this->vertices[i][0] = this->vertices[i][0] + (1 / (distX*distX));
    } else if(distX <= -1) {
      this->vertices[i][0] = this->vertices[i][0] - (1 / (distX*distX));
    }
    if(distY >= 1){
      this->vertices[i][1] = this->vertices[i][1] + (1 / (distY*distY));
    } else if(distY <= -1) {
      this->vertices[i][1] = this->vertices[i][1] - (1 / (distY*distY));
    }
    if(distZ >= 1){
      this->vertices[i][2] = this->vertices[i][2] + (1 / (distZ*distZ));
    } else if(distZ <= -1) {
      this->vertices[i][2] = this->vertices[i][2] - (1 / (distZ*distZ));
    }
  }
}
/*
void Mesh::sphereWarp(const Sphere &s){
  float x,y,z;
  float xy, nx, ny, nz;
  float lengthInv = 1.0/s.getRadius();
  float pasH = 2*M_PI/s.getNbHorizontal();
  float pasV = M_PI/s.getNbVertical();
  float angleH;
  float angleV;
  std::vector<Vector> sphereVertices;
  for(int i=0; i<=s.getNbVertical(); i++){
    angleV = M_PI / 2 - i * pasV;
    xy = s.getRadius()*cosf(angleV);
    z = s.getRadius()*sinf(angleV) + s.getCenter()[2];

    for(int j=0; j<=s.getNbHorizontal(); j++) {
      angleH = j * pasH;

      x = xy * cosf(angleH) + s.getCenter()[0];
      y = xy * sinf(angleH) + s.getCenter()[1];
      sphereVertices.push_back(Vector(x,y,z));
    }
  }
  for(int i=0; i<this->vertices.size(); i++) {
    for(int j=0; sphereVertices.size(), j++) {
      if(vertices[i] == sphereVertices[j]) {
        double dist = sqrt((pow(s.getCenter()[0]-this->vertices[i][0],2)) + (pow(s.getCenter()[1] - this->vertices[i][1], 2)) + (pow(s.getCenter()[2] - this->vertices[i][2], 2)));
        double distX = s.getCenter()[0] - this->vertices[i][0];
        double distY = s.getCenter()[1] - this->vertices[i][1];
        double distZ = s.getCenter()[2] - this->vertices[i][2];
        if(distX > 0){
          this->vertices[i][0] = this->vertices[i][0] + (1 / (distX*distX));
        } else if(distX < 0) {
          this->vertices[i][0] = this->vertices[i][0] - (1 / (distX*distX));
        }
        if(distY > 0){
          this->vertices[i][1] = this->vertices[i][1] + (1 / (distY*distY));
        } else if(distY < 0) {
          this->vertices[i][1] = this->vertices[i][1] - (1 / (distY*distY));
        }
        if(distZ > 0){
          this->vertices[i][2] = this->vertices[i][2] + (1 / (distZ*distZ));
        } else if(distZ < 0) {
          this->vertices[i][2] = this->vertices[i][2] - (1 / (distZ*distZ));
        }
      }
    }
  }
}*/

/*!
\brief Scale the mesh.
\param s Scaling factor.
*/
void Mesh::Scale(double s)
{
    // Vertexes
    for (int i = 0; i < vertices.size(); i++)
    {
        vertices[i] *= s;
    }

    if (s < 0.0)
    {
        // Normals
        for (int i = 0; i < normals.size(); i++)
        {
            normals[i] = -normals[i];
        }
    }
}



#include <QtCore/QFile>
#include <QtCore/QTextStream>
#include <QtCore/QRegularExpression>
#include <QtCore/qstring.h>

/*!
\brief Import a mesh from an .obj file.
\param filename File name.
*/
void Mesh::Load(const QString& filename)
{
  vertices.clear();
  normals.clear();
  varray.clear();
  narray.clear();

  QFile data(filename);

  if (!data.open(QFile::ReadOnly))
    return;
  QTextStream in(&data);

  // Set of regular expressions : Vertex, Normal, Triangle
  QRegularExpression rexv("v\\s*([-|+|\\s]\\d*\\.\\d+)\\s*([-|+|\\s]\\d*\\.\\d+)\\s*([-|+|\\s]\\d*\\.\\d+)");
  QRegularExpression rexn("vn\\s*([-|+|\\s]\\d*\\.\\d+)\\s*([-|+|\\s]\\d*\\.\\d+)\\s*([-|+|\\s]\\d*\\.\\d+)");
  QRegularExpression rext("f\\s*(\\d*)/\\d*/(\\d*)\\s*(\\d*)/\\d*/(\\d*)\\s*(\\d*)/\\d*/(\\d*)");
  while (!in.atEnd())
  {
    QString line = in.readLine();
    QRegularExpressionMatch match = rexv.match(line);
    QRegularExpressionMatch matchN = rexn.match(line);
    QRegularExpressionMatch matchT = rext.match(line);
    if (match.hasMatch())//rexv.indexIn(line, 0) > -1)
    {
      Vector q = Vector(match.captured(1).toDouble(), match.captured(2).toDouble(), match.captured(3).toDouble()); vertices.push_back(q);
    }
    else if (matchN.hasMatch())//rexn.indexIn(line, 0) > -1)
    {
      Vector q = Vector(matchN.captured(1).toDouble(), matchN.captured(2).toDouble(), matchN.captured(3).toDouble());  normals.push_back(q);
    }
    else if (matchT.hasMatch())//rext.indexIn(line, 0) > -1)
    {
      varray.push_back(matchT.captured(1).toInt() - 1);
      varray.push_back(matchT.captured(3).toInt() - 1);
      varray.push_back(matchT.captured(5).toInt() - 1);
      narray.push_back(matchT.captured(2).toInt() - 1);
      narray.push_back(matchT.captured(4).toInt() - 1);
      narray.push_back(matchT.captured(6).toInt() - 1);
    }
  }
  data.close();
}

/*!
\brief Save the mesh in .obj format, with vertices and normals.
\param url Filename.
\param meshName %Mesh name in .obj file.
*/
void Mesh::SaveObj(const QString& url, const QString& meshName) const
{
  QFile data(url);
  if (!data.open(QFile::WriteOnly))
    return;
  QTextStream out(&data);
  out << "g " << meshName << Qt::endl;
  for (int i = 0; i < vertices.size(); i++)
    out << "v " << vertices.at(i)[0] << " " << vertices.at(i)[1] << " " << vertices.at(i)[2] << QString('\n');
  for (int i = 0; i < normals.size(); i++)
    out << "vn " << normals.at(i)[0] << " " << normals.at(i)[1] << " " << normals.at(i)[2] << QString('\n');
  for (int i = 0; i < varray.size(); i += 3)
  {
    out << "f " << varray.at(i) + 1 << "//" << narray.at(i) + 1 << " "
      << varray.at(i + 1) + 1 << "//" << narray.at(i + 1) + 1 << " "
      << varray.at(i + 2) + 1 << "//" << narray.at(i + 2) + 1 << " "
      << "\n";
  }
  out.flush();
  data.close();
}

